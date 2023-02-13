package mcc;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import mcc.config.ConfigBuilder;
import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeCommand;
import mcc.decisiondome.DecisionDomeTemplate;

public class MCCTest extends JavaPlugin {
	
	private DecisionDome decisionDome;
	
	private ConfigBuilder configBuilder;
	
	private DecisionDomeCommand domeCommand;
	
	private int schedulerId;
	
	// Templates
	private DecisionDomeTemplate decisionDomeTemplate;
	
	@Override
	public void onEnable() {
		this.configBuilder = new ConfigBuilder();
		
		this.decisionDomeTemplate = new DecisionDomeTemplate(new ArrayList<>()); // TODO: Load from config
		this.decisionDome = new DecisionDome(this, this.decisionDomeTemplate);
		
		getCommand("decisiondome").setExecutor(this.domeCommand = new DecisionDomeCommand(this));
		
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
	
	public DecisionDomeTemplate getDecisionDomeTemplate() {
		return decisionDomeTemplate;
	}
	
	public DecisionDome getDecisionDome() {
		return decisionDome;
	}
}
