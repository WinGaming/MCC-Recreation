package mcc;

import java.io.IOException;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mcc.config.ConfigBuilder;
import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeCommand;
import mcc.decisiondome.DecisionDome.DecisionDomeState;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
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
	private MCCState state = MCCState.DECISIONDOME_RUNNING;

	private Pair<Supplier<String>, Supplier<Timer>> generateScoreboardDisplaySuppliers() {
		Supplier<String> titleSupplier = () -> {
			switch (this.state) {
				case EVENT_NOT_STARTED:
				case EVENT_STARTING:
					return ChatColor.RED + "" + ChatColor.BOLD + "Event begins in:";
				case DECISIONDOME_COUNTDOWN:
					return ChatColor.RED + "" + ChatColor.BOLD + "Decision Dome in:";
				case DECISIONDOME_RUNNING: {
					DecisionDomeState decisionDomeState = this.decisionDome.getState();
					switch (decisionDomeState) {
						case WAITING:
							return "null";
						case GAME_SELECTION_INTRO:
							return ChatColor.RED + "" + ChatColor.BOLD + "Voting begins in:";
						case GAME_SELECTION:
							return ChatColor.RED + "" + ChatColor.BOLD + "Voting closes in:";
						case GAME_SELECTION_FINAL:
						case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT:
						case GAME_SELECTED:
							return ChatColor.RED + "" + ChatColor.BOLD + "Game chosen in:";
						case GAME_SELECTED_AWAIT_TELEPORT:
							return ChatColor.RED + "" + ChatColor.BOLD + "Teleporting to Game in:";
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
		this.lobbyTemplate = new CachedScoreboardTemplate(IChatBaseComponent.literal(ChatColor.YELLOW + "" + ChatColor.BOLD + "MC Championship Pride 22"), new ScoreboardPartProvider[] {
            new SuppliedTimerScoreboardPartProvider(scoreboardDisplaySuppliers.getA(), scoreboardDisplaySuppliers.getB()),
            // (e) -> new Pair<>(new String[] { ChatColor.GREEN + "" + ChatColor.BOLD + "Players: " + ChatColor.RESET +  "0/40"}, 0L),
            // (e) -> new Pair<>(new String[] {
            //     ChatColor.WHITE + "" + ChatColor.BOLD + "Your Team:",
            //     ChatColor.RESET + "" + temaplte.getIcon() + " " + temaplte.getName() // We need to add reset, because minecraft hides names that start with #
            // }, 0L),
            // (e) -> new Pair<>(new String[] {
            //     ChatColor.GREEN + "" + ChatColor.BOLD + "Last Event Coins: " + ChatColor.RESET + "1866~",
            //     ChatColor.GREEN + "" + ChatColor.BOLD + "Lifetime Coins: " + ChatColor.RESET + "28420~"
            // }, 0L),
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
