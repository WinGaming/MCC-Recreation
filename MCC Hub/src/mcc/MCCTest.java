package mcc;

import static mcc.stats.PlayerStatsManager.getEventCoins;
import static mcc.stats.PlayerStatsManager.getLifetimeCoins;
import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.RESET;
import static org.bukkit.ChatColor.YELLOW;

import java.io.IOException;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mcc.config.ConfigBuilder;
import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDome.DecisionDomeState;
import mcc.decisiondome.DecisionDomeCommand;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
import mcc.display.TeamScoreboardPartProvider;
import mcc.display.TeamsPlayerCountScoreboardPartProvider;
import mcc.teams.TeamManager;
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.yml.HubConfig;
import net.minecraft.network.chat.IChatBaseComponent;

public class MCCTest extends JavaPlugin {
	
	private DecisionDome decisionDome;
	
	private ConfigBuilder configBuilder;
	private DecisionDomeCommand domeCommand;
	
	private int schedulerId;
	
	private HubConfig config;

	private TeamManager teamManager;
	
	private Timer lobbyTimer = null;
	private MCCState state = MCCState.EVENT_NOT_STARTED;

	// TODO:
	private String lastEventId;
	private String currentEventId;

	private Pair<Supplier<String>, Supplier<Timer>> generateScoreboardDisplaySuppliers() {
		Supplier<String> titleSupplier = () -> {
			switch (this.state) {
				case EVENT_NOT_STARTED:
				case EVENT_STARTING:
					return RED + "" + BOLD + "Event begins in:";
				case DECISIONDOME_COUNTDOWN:
					return RED + "" + BOLD + "Decision Dome in:";
				case DECISIONDOME_RUNNING: {
					DecisionDomeState decisionDomeState = this.decisionDome.getState();
					switch (decisionDomeState) {
						case WAITING:
							return "null";
						case GAME_SELECTION_INTRO:
							return RED + "" + BOLD + "Voting begins in:";
						case GAME_SELECTION:
							return RED + "" + BOLD + "Voting closes in:";
						case GAME_SELECTION_FINAL:
						case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT:
						case GAME_SELECTED:
							return RED + "" + BOLD + "Game chosen in:";
						case GAME_SELECTED_AWAIT_TELEPORT:
							return RED + "" + BOLD + "Teleporting to Game in:";
						default:
							return "null";
					}
				}
				default: return "null";
			}
		};

		Supplier<Timer> timerSupplier = () -> {
			switch (this.state) {
				case EVENT_NOT_STARTED:
				case EVENT_STARTING: 
				case DECISIONDOME_COUNTDOWN:
					return this.lobbyTimer;
				case DECISIONDOME_RUNNING:
					return this.decisionDome.getCurrentTimer();
				default:
					return null;
			}
		};

		return new Pair<>(titleSupplier, timerSupplier);
	}

	private CachedScoreboardTemplate lobbyTemplate;

	@Override
	public void onEnable() {
		this.configBuilder = new ConfigBuilder();
		
		try {
			this.config = new HubConfig();
		} catch (IOException e) {
			System.err.println("Failed to load configuration! See error message for more details");
			
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			Bukkit.getServer().shutdown();
			
			return;
		}
		
		this.decisionDome = new DecisionDome(this, this.config.getDecisiondome());
		
		getCommand("decisiondome").setExecutor(this.domeCommand = new DecisionDomeCommand(this, this.config));
		
		getServer().getPluginManager().registerEvents(this.configBuilder, this);
		
		this.teamManager = new TeamManager();
		getServer().getPluginManager().registerEvents(this.teamManager, this);

		this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);

		Pair<Supplier<String>, Supplier<Timer>> scoreboardDisplaySuppliers = this.generateScoreboardDisplaySuppliers();
		this.lobbyTemplate = new CachedScoreboardTemplate(IChatBaseComponent.literal(YELLOW + "" + BOLD + "MC Championship Pride 22"), new ScoreboardPartProvider[] {
            new SuppliedTimerScoreboardPartProvider(scoreboardDisplaySuppliers.getA(), scoreboardDisplaySuppliers.getB()),
            new TeamsPlayerCountScoreboardPartProvider(this.teamManager),
            new TeamScoreboardPartProvider(this.teamManager),
			(uuid) -> {
				String eventCoinsString;
				if (this.state == MCCState.EVENT_NOT_STARTED) {
					eventCoinsString = GREEN + "" + BOLD + "Last Event Coins: " + RESET + "" + getEventCoins(this.lastEventId, uuid) + "~";
				} else {
					eventCoinsString = GREEN + "" + BOLD + "Event Coins: " + RESET + "" + getEventCoins(this.currentEventId, uuid) + "~";
				}

				String lifetimeString = GREEN + "" + BOLD + "Lifetime Coins: " + RESET + getLifetimeCoins(uuid) + "~";

				return new Pair<>(new String[] { eventCoinsString, lifetimeString }, System.currentTimeMillis());
			},
        });
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(schedulerId);
	}
	
	private void tick() {
		this.decisionDome.tick();
		this.domeCommand.tick();

		for (Player player : Bukkit.getOnlinePlayers()) {
			this.lobbyTemplate.show(player);
		}
	}
	
	public ConfigBuilder getConfigBuilder() {
		return configBuilder;
	}
	
	public DecisionDome getDecisionDome() {
		return decisionDome;
	}

	public enum MCCState {
		/** The game wasn't started yet */
		EVENT_NOT_STARTED,
		/** The game is starting, give everyone time to prepare */
		EVENT_STARTING,
		/** Time to go to the decision dome */
		DECISIONDOME_COUNTDOWN,
		/** The decision dome is running, and it manages everything on its own */
		DECISIONDOME_RUNNING,
	}
}
