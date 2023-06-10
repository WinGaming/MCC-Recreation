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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeUtils;
import mcc.decisiondome.items.VoteEgg;
import mcc.decisiondome.selector.EntityFieldSelector;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
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
import mcc.utils.Vector3d;
import mcc.yml.decisiondome.FileConfig;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.lobby.HubLobbyConfig;
import net.minecraft.network.chat.IChatBaseComponent;

public class Event implements Listener {
    
    private Timer lobbyTimer;
    private EventState currentState;
    
	private final String eventId;
	private final String lastEventId;
    
    private final GameTask gameTask;
    private final TeamManager teamManager;
    private final DecisionDome decisionDome;
    
    private final CachedScoreboardTemplate lobbyTemplate;

    private Location spawnLocation;

    // TODO: THis is just temp
    private int currentRound = 0;

    @EventHandler
    public void iTemp(PlayerInteractEvent event) {
        if (event.getItem().getType() == Material.EGG && event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            new VoteEgg().onInteraction(event.getPlayer());
            event.setCancelled(true);
        }
    }

    public static Event fromStats(String eventId, EventStats stats, FileConfig<HubDecisiondomeConfig> config, FileConfig<HubLobbyConfig> lobbyConfig) {
        Optional<List<PreparedTeam>> teams = stats.getTeamsForEvent(eventId);
        Optional<EventRecord> lastEvent = stats.getLastEventBefore(eventId);

        if (teams.isEmpty()) {
            throw new IllegalArgumentException("No teams found for event " + eventId);
        }

        return new Event(eventId, lastEvent.isPresent() ? lastEvent.get().getEventId() : null, new TeamManager(teams.get()), config, lobbyConfig);
    }

    private Event(String id, String lastEvent, TeamManager teamManager, FileConfig<HubDecisiondomeConfig> config, FileConfig<HubLobbyConfig> lobbyConfig) {
        this.eventId = id;
        this.lastEventId = lastEvent;

        World world = Bukkit.getWorld(lobbyConfig.getConfigInstance().getWorldName());
        if (world == null) {
			throw new IllegalArgumentException("Failed to reload: Could not find world \"" + lobbyConfig.getConfigInstance().getWorldName() + "\"");
		}
        Optional<Vector3d> spawnVector = lobbyConfig.getConfigInstance().getSpawnLocation();
        if (spawnVector.isEmpty()) {
            throw new IllegalArgumentException("Failed to reload: Spawn location not set");
        }
        this.spawnLocation = new Location(world, spawnVector.get().getX(), spawnVector.get().getY(), spawnVector.get().getZ()); // TODO: Yaw and pitch
        
        this.gameTask = new GameTask();

        this.teamManager = teamManager;
        this.currentState = EventState.NOT_STARTED;
        this.decisionDome = DecisionDomeUtils.loadFromConfig(this, config.getConfigInstance(), this.teamManager, new EntityFieldSelector(), this.gameTask);

        this.lobbyTemplate = new CachedScoreboardTemplate(IChatBaseComponent.literal(YELLOW + "" + BOLD + "MC Championship Pride 22"), new ScoreboardPartProvider[] {
            new SuppliedTimerScoreboardPartProvider(this::getTimerTitle, this::getTimer),
            new TeamsPlayerCountScoreboardPartProvider(this.teamManager),
            new TeamScoreboardPartProvider(this.teamManager),
			(uuid) -> {
				String eventCoinsString;
				if (this.currentState == EventState.NOT_STARTED) {
					eventCoinsString = GREEN + "" + BOLD + "Last Event Coins: " + RESET + "" + getEventCoins(this.getPreviousEventId(), uuid) + "~";
				} else {
					eventCoinsString = GREEN + "" + BOLD + "Event Coins: " + RESET + "" + getEventCoins(this.getId(), uuid) + "~";
				}

				String lifetimeString = GREEN + "" + BOLD + "Lifetime Coins: " + RESET + getLifetimeCoins(uuid) + "~";

				return new Pair<>(new String[] { eventCoinsString, lifetimeString }, System.currentTimeMillis());
			},
        });
    }

    public void switchToGame() {
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
        this.gameTask.tick();

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

        for (Player player : Bukkit.getOnlinePlayers()) {
			this.lobbyTemplate.show(player);
		}
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

    private String getTimerTitle() {
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

    private Timer getTimer() {
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

    @EventHandler
    public void onEggBreak(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == SpawnReason.EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEggBreak(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.EGG) {
            Egg egg = (Egg) event.getEntity();
            egg.remove();
            
            Location spawnLocation = egg.getLocation().clone().add(0, 0.5, 0);
            Chicken voteChicken = (Chicken) egg.getWorld().spawnEntity(spawnLocation, EntityType.CHICKEN);
            
            // TODO: Replace this with team color etc.
            voteChicken.setAI(false);
            voteChicken.setGravity(false);
            voteChicken.setCustomName(egg.getCustomName());
            voteChicken.setCustomNameVisible(true);

            event.setCancelled(true);
        }
    }
}
