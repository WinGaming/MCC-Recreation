package mcc;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import mcc.config.ConfigBuilder;
import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeCommand;
import mcc.yml.HubConfig;

public class MCCTest extends JavaPlugin {
	
	private DecisionDome decisionDome;
	
	private ConfigBuilder configBuilder;
	
	private DecisionDomeCommand domeCommand;
	
	private int schedulerId;
	
	private HubConfig config;
	
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
		
		this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(schedulerId);
	}
	
	private void tick() {
		this.decisionDome.tick();
		this.domeCommand.tick();
	}
	
	public ConfigBuilder getConfigBuilder() {
		return configBuilder;
	}
	
	public DecisionDome getDecisionDome() {
		return decisionDome;
	}
}
