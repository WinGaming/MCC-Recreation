package mcc.event;

import static mcc.stats.PlayerStatsManager.getEventCoins;
import static mcc.stats.PlayerStatsManager.getLifetimeCoins;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.YELLOW;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mcc.decisiondome.DecisionDome;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
import mcc.display.TeamScoreboardPartProvider;
import mcc.display.TeamsPlayerCountScoreboardPartProvider;
import mcc.teams.TeamManager;
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.yml.hub.HubConfig;
import net.minecraft.network.chat.IChatBaseComponent;

public class Event {
    
    private EventState currentState;
    private CachedScoreboardTemplate lobbyTemplate;

    private Timer lobbyTimer = null;
    private DecisionDome decisionDome;

	private String eventId;
	private String lastEventId;

    private TeamManager teamManager;

    private Event(TeamManager teamManager, HubConfig config) {
        this.teamManager = teamManager;

        this.currentState = EventState.NOT_STARTED;
        this.decisionDome = new DecisionDome(config.getDecisiondome());

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
        this.decisionDome.tick();

        for (Player player : Bukkit.getOnlinePlayers()) {
			this.lobbyTemplate.show(player);
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
