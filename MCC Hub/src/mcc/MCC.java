package mcc;

import static mcc.yml.ConfigUtils.loadConfig;

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
import mcc.yml.FileConfig;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.event.EventConfig;
import mcc.yml.lobby.HubLobbyConfig;

public class MCC extends JavaPlugin implements Listener {
	
	// Static load error handling
	private static boolean failedInitialization = false;
	public static void markStaticLoadError() { MCC.failedInitialization = true; }

	// Config instances
	public static final FileConfig<HubDecisiondomeConfig> decisiondomeConfig = loadConfig("decisiondome", new HubDecisiondomeConfig());
	public static final FileConfig<HubLobbyConfig> lobbyConfig = loadConfig("lobby", new HubLobbyConfig());
	public static final FileConfig<EventConfig> eventConfig = loadConfig("event", new EventConfig());

	// Event instances
	private Event eventInstance;
	
	// final instances
	private int schedulerId;
	private ConfigBuilder configBuilder;

	@Override
	public void onEnable() {
		if (failedInitialization) {
			throw new IllegalStateException("Unknown error occured while loading MCC. See error message above for more details");
		}

		this.configBuilder = new ConfigBuilder();
		
		getCommand("decisiondome").setExecutor(new DecisionDomeCommand(this.configBuilder));
		getCommand("mcc").setExecutor(new MCCCommand(this));
		
		getServer().getPluginManager().registerEvents(this.configBuilder, this);
		getServer().getPluginManager().registerEvents(this, this);
		
		this.schedulerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 0, 1);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerTagCache.clearTags(event.getPlayer().getUniqueId());
	}

	public void startEvent(String eventId) { // TODO: Return boolean state
		this.eventInstance = Event.fromStats(eventId, new ExampleEventStats()); // TODO: Use real stats
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

	public Event getEventInstance() {
		return eventInstance;
	}
}
