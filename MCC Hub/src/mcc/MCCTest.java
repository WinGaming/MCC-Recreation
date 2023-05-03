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
import mcc.event.Event;
import mcc.teams.TeamManager;
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.yml.hub.HubConfig;
import net.minecraft.network.chat.IChatBaseComponent;

public class MCCTest extends JavaPlugin {
	
	private Event eventInstance;

	private ConfigBuilder configBuilder;
	private DecisionDomeCommand domeCommand;
	
	private int schedulerId;
	
	private HubConfig config;

	private TeamManager teamManager;

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
		
		getCommand("decisiondome").setExecutor(this.domeCommand = new DecisionDomeCommand(this, this.config));
		
		getServer().getPluginManager().registerEvents(this.configBuilder, this);
		
		this.teamManager = new TeamManager();
		getServer().getPluginManager().registerEvents(this.teamManager, this);

		this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(schedulerId);
	}
	
	private void tick() {
		this.eventInstance.tick();
		this.domeCommand.tick();
	}
	
	public ConfigBuilder getConfigBuilder() {
		return configBuilder;
	}
}
