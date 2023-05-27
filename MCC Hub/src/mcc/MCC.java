package mcc;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import mcc.commands.DecisionDomeCommand;
import mcc.commands.MCCCommand;
import mcc.config.ConfigBuilder;
import mcc.event.Event;
import mcc.yml.hub.HubConfig;

public class MCC extends JavaPlugin implements Listener {
	
	// Event instances
	private Event eventInstance;
	
	// final instances
	private int schedulerId;
	private ConfigBuilder configBuilder;
	
	// Config instances
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
		
		getCommand("decisiondome").setExecutor(new DecisionDomeCommand(this.configBuilder, this.config));
		getCommand("mcc").setExecutor(new MCCCommand(this.config));
		
		getServer().getPluginManager().registerEvents(this.configBuilder, this);
		
		this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(schedulerId);
	}
	
	private void tick() {
		if (this.eventInstance != null) {
			this.eventInstance.tick();
		}
		
		this.configBuilder.tick();
	}
	
	public ConfigBuilder getConfigBuilder() {
		return configBuilder;
	}

	public Event getEventInstance() {
		return eventInstance;
	}
}
