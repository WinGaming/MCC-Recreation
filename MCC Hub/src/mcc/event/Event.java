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
import org.bukkit.entity.Player;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeUtils;
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
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.yml.hub.HubConfig;
import net.minecraft.network.chat.IChatBaseComponent;

public class Event {
    
    private Timer lobbyTimer;
    private EventState currentState;
    
	private final String eventId;
	private final String lastEventId;
    
    private final GameTask gameTask;
    private final TeamManager teamManager;
    private final DecisionDome decisionDome;
    
    private final CachedScoreboardTemplate lobbyTemplate;

    public static Event fromStats(String eventId, EventStats stats, HubConfig config) {
        Optional<List<PreparedTeam>> teams = stats.getTeamsForEvent(eventId);
        Optional<EventRecord> lastEvent = stats.getLastEventBefore(eventId);

        if (teams.isEmpty()) {
            throw new IllegalArgumentException("No teams found for event " + eventId);
        }

        return new Event(eventId, lastEvent.isPresent() ? lastEvent.get().getEventId() : null, new TeamManager(teams.get()), config);
    }

    private Event(String id, String lastEvent, TeamManager teamManager, HubConfig config) {
        this.eventId = id;
        this.lastEventId = lastEvent;
        
        this.gameTask = new GameTask();

        this.teamManager = teamManager;
        this.currentState = EventState.NOT_STARTED;
        this.decisionDome = DecisionDomeUtils.loadFromConfig(config.getDecisiondome(), this.teamManager, new EntityFieldSelector(), this.gameTask);

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

    public void tick() {
        long now = System.currentTimeMillis();

        if (this.lobbyTimer != null && this.lobbyTimer.remaining(now) <= 0) {
            switch (this.currentState) {
                case STARTING:
                    this.currentState = EventState.DECISIONDOME_COUNTDOWN;
                    this.lobbyTimer = new Timer(TimeUnit.SECONDS, 60); // TODO: Config
                    break;
                case DECISIONDOME_COUNTDOWN:
                    this.currentState = EventState.DECISIONDOME_RUNNING;
                    this.decisionDome.start();
                    break;
                case NOT_STARTED:
                case DECISIONDOME_RUNNING:
                        this.lobbyTimer = null;
                        break;
            }
        }

        this.decisionDome.tick();
        this.gameTask.tick();

        for (Player player : Bukkit.getOnlinePlayers()) {
			this.lobbyTemplate.show(player);
		}
    }

    public void start() {
        if (this.currentState == EventState.NOT_STARTED) {
            this.currentState = EventState.STARTING;
            this.lobbyTimer = new Timer(TimeUnit.SECONDS, 60); // TODO: Config
        } else {
            throw new IllegalStateException("Cannot start event as it is already running");
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
                return RED + "" + BOLD + "Decision Dome in:";
            case DECISIONDOME_RUNNING: {
                return this.decisionDome.getTimerTitle();
            }
            default: return "null";
        }
    }

    private Timer getTimer() {
        switch (this.currentState) {
            case NOT_STARTED:
            case STARTING: 
            case DECISIONDOME_COUNTDOWN:
                return this.lobbyTimer;
            case DECISIONDOME_RUNNING:
                return this.decisionDome.getCurrentTimer();
            default:
                return null;
        }
    }
}
