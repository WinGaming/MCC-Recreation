package mcc.event;

import static mcc.stats.PlayerStatsManager.getEventCoins;
import static mcc.stats.PlayerStatsManager.getLifetimeCoins;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.YELLOW;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mcc.MCC;
import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeUtils;
import mcc.decisiondome.selector.EntityFieldSelector;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
import mcc.display.TablistDisplay;
import mcc.display.TeamScoreboardPartProvider;
import mcc.display.TeamsPlayerCountScoreboardPartProvider;
import mcc.game.GameTask;
import mcc.stats.EventStats;
import mcc.stats.record.EventRecord;
import mcc.teams.TeamManager;
import mcc.timer.EmptyTimer;
import mcc.timer.Timer;
import mcc.timer.scripts.ScriptManager;
import mcc.utils.Pair;
import mcc.yml.ConfigInstanceUtils;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;

public class Event implements Listener {
    
	private final String eventId;
	private final String lastEventId;
    
    private final GameTask gameTask;
    private final TeamManager teamManager;
    private final DecisionDome decisionDome;
    
    private final CachedScoreboardTemplate lobbyTemplate;

    private Timer lobbyTimer;
    private EventState currentState;
    private Location spawnLocation;

    // TODO: THis is just temp
    private int currentRound = 0;

    public TeamManager getTeamManager() {
        return teamManager;
    }

    protected Event(String id, String lastEvent, TeamManager teamManager, IChatBaseComponent title) {
        this.eventId = id;
        this.lastEventId = lastEvent;
        this.teamManager = teamManager;

        this.gameTask = new GameTask();
        this.currentState = EventState.NOT_STARTED;
        
        this.spawnLocation = ConfigInstanceUtils.instantiateHubSpawnLocation();
        this.decisionDome = DecisionDomeUtils.loadFromConfig(this, this.teamManager, new EntityFieldSelector());
        this.lobbyTemplate = new CachedScoreboardTemplate(title, "lobby", MCC.eventConfig.getConfigInstance().getLobbyDisplay().getScoreboardParts(this));
    }

    public ScoreboardPartProvider getTempCoinsProvider() {
        return (uuid) -> {
            String eventCoinsString;
            if (this.currentState == EventState.NOT_STARTED) {
                eventCoinsString = GREEN + "" + BOLD + "Last Event Coins: " + RESET + getEventCoins(this.getPreviousEventId(), uuid) + "~";
            } else {
                eventCoinsString = GREEN + "" + BOLD + "Event Coins: " + RESET + getEventCoins(this.getId(), uuid) + "~";
            }

            String lifetimeString = GREEN + "" + BOLD + "Lifetime Coins: " + RESET + getLifetimeCoins(uuid) + "~";

            return new Pair<>(new String[] { eventCoinsString, lifetimeString }, System.currentTimeMillis());
        };
    }

    public void switchToGame() {
        for (Player player : Bukkit.getOnlinePlayers()) this.lobbyTemplate.hide(player);
        this.gameTask.teleportPlayers();
        this.currentState = EventState.MINIGAME;
    }

    public void tick() {
        long now = System.currentTimeMillis();

        if (this.lobbyTimer != null && this.lobbyTimer.remaining(now) <= 0) {
            switch (this.currentState) {
                case STARTING:
                    this.currentState = EventState.DECISIONDOME_COUNTDOWN;
                    this.lobbyTimer = new Timer(TimeUnit.SECONDS, 61); // TODO: Config
                    this.lobbyTimer.start(now);
                    break;
                case DECISIONDOME_COUNTDOWN:
                    if (this.currentRound == 0) {
                        this.currentState = EventState.DECISIONDOME_COUNTDOWN_PARTY;
                        this.lobbyTimer = new EmptyTimer(TimeUnit.SECONDS, 15); // TODO: Config
                        this.lobbyTimer.start(now);
                    } else {
                        this.currentState = EventState.DECISIONDOME_RUNNING;
                        this.decisionDome.start();
                    }
                    break;
                case DECISIONDOME_COUNTDOWN_PARTY:
                    this.currentState = EventState.DECISIONDOME_RUNNING;
                    this.decisionDome.start();
                    break;
                case NOT_STARTED:
                case DECISIONDOME_RUNNING:
                case MINIGAME:
                case PAUSED:
                case PAUSED_IN_MINIGAME:
                    this.lobbyTimer = null;
                    break;
            }
        }

        this.decisionDome.tick(now);
        if (this.currentState == EventState.MINIGAME && this.gameTask.tick(now)) {
            this.backToLobby();
            this.currentState = EventState.DECISIONDOME_COUNTDOWN;
        }

        switch (this.currentState) {
            case DECISIONDOME_RUNNING:
                ScriptManager.tick("decisiondome", this.decisionDome.getState().name(), this.decisionDome.getCurrentTimer(), now);
                break;
            case MINIGAME:
                ScriptManager.tick("minigame", null, null, now);
                break;
            case NOT_STARTED:
            case PAUSED:
            case PAUSED_IN_MINIGAME:
            case STARTING:
            case DECISIONDOME_COUNTDOWN:
            case DECISIONDOME_COUNTDOWN_PARTY:
                ScriptManager.tick("event", this.currentState.name(), lobbyTimer, now);
                break;
        }

        if (this.currentState != EventState.MINIGAME) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                this.lobbyTemplate.show(player);
            }
        }
    }

    public boolean prepareMinigame(String game) {
        return this.gameTask.prepareGame(game, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.handlePlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.teamManager.handleQuit(event.getPlayer());
    }

    public void handlePlayerJoin(Player player) {
        boolean joinResult = this.teamManager.joinIfRegistered(player);
        if (!joinResult) {
            player.sendMessage("You are not registered for this event");
            player.setGameMode(GameMode.SPECTATOR);
        }

        PacketPlayOutPlayerListHeaderFooter a = new TablistDisplay().generatePacket(this.teamManager.getTeams(), 400);
        ((CraftPlayer) player).getHandle().connection.send(a);

        player.teleport(this.spawnLocation);
    }

    private void backToLobby() {
        this.decisionDome.stop();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(this.spawnLocation);
        }
    }

    public void pause() {
        switch (this.currentState) {
            case DECISIONDOME_RUNNING:
                this.backToLobby();
                this.currentState = EventState.PAUSED;
                break;
            case NOT_STARTED:
                this.currentState = EventState.PAUSED;
                break; // We are already in a paused state
            case STARTING:
            case DECISIONDOME_COUNTDOWN:
            case DECISIONDOME_COUNTDOWN_PARTY:
                this.lobbyTimer = null;
                this.currentState = EventState.PAUSED;
                break;
            case MINIGAME:
                this.currentState = EventState.PAUSED_IN_MINIGAME;
                break; // TODO:
            case PAUSED:
            case PAUSED_IN_MINIGAME:
                break; // Already in a paused state
        }
    }

    public boolean resume() {
        if (this.currentState == EventState.NOT_STARTED) {
            this.currentState = EventState.STARTING;
            this.lobbyTimer = new Timer(TimeUnit.SECONDS, 59, 61); // TODO: Config
            this.lobbyTimer.start(System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }

    public String getId() {
        return eventId;
    }

    public String getPreviousEventId() {
        return lastEventId;
    }

    public String getTimerTitle() {
        switch (this.currentState) {
            case NOT_STARTED:
            case STARTING:
                return RED + "" + BOLD + "Event begins in:";
            case DECISIONDOME_COUNTDOWN:
            case DECISIONDOME_COUNTDOWN_PARTY:
                return RED + "" + BOLD + "Decision Dome in:";
            case DECISIONDOME_RUNNING:
                return this.decisionDome.getTimerTitle();
            case PAUSED:
            case PAUSED_IN_MINIGAME:
                return "Paused";
            default: return "null";
        }
    }

    public Timer getTimer() {
        switch (this.currentState) {
            case NOT_STARTED:
            case STARTING: 
            case DECISIONDOME_COUNTDOWN:
            case DECISIONDOME_COUNTDOWN_PARTY:
                return this.lobbyTimer;
            case DECISIONDOME_RUNNING:
                return this.decisionDome.getCurrentTimer();
            default:
                return null;
        }
    }
}
