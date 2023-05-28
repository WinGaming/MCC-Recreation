package mcc;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import mcc.commands.DecisionDomeCommand;
import mcc.commands.MCCCommand;
import mcc.config.ConfigBuilder;
import mcc.event.Event;
import mcc.utils.PlayerTagCache;
import mcc.yml.decisiondome.FileConfig;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.lobby.HubLobbyConfig;

public class MCC extends JavaPlugin implements Listener {
	
	// Event instances
	private Event eventInstance;
	
	// final instances
	private int schedulerId;
	private ConfigBuilder configBuilder;
	
	// Config instances
	private FileConfig<HubDecisiondomeConfig> decisiondomeConfig;
	private FileConfig<HubLobbyConfig> lobbyConfig;

	@Override
	public void onEnable() {
		this.configBuilder = new ConfigBuilder();
		
		try {
			this.decisiondomeConfig = new FileConfig<HubDecisiondomeConfig>("decisiondome", new HubDecisiondomeConfig());
			this.lobbyConfig = new FileConfig<HubLobbyConfig>("lobby", new HubLobbyConfig());
		} catch (IOException e) {
			System.err.println("Failed to load config-files! See error message for more details");
			
			e.printStackTrace();
			
			Bukkit.getPluginManager().disablePlugin(this);
			Bukkit.getServer().shutdown();
			
			return;
		}
		
		getCommand("decisiondome").setExecutor(new DecisionDomeCommand(this.configBuilder, this.decisiondomeConfig));
		getCommand("mcc").setExecutor(new MCCCommand(this, this.decisiondomeConfig));
		
		getServer().getPluginManager().registerEvents(this.configBuilder, this);
		getServer().getPluginManager().registerEvents(this, this);
		
		this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerTagCache.clearTags(event.getPlayer().getUniqueId());
	}

	public void startEvent(String eventId) { // TODO: Return boolean state
		this.eventInstance = Event.fromStats(eventId, new ExampleEventStats(), this.decisiondomeConfig, this.lobbyConfig); // TODO: Use real stats
		getServer().getPluginManager().registerEvents(this.eventInstance, this);

		for (Player player : getServer().getOnlinePlayers()) {
			this.eventInstance.handlePlayerJoin(player);
		}
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
