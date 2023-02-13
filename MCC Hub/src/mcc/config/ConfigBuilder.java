package mcc.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ConfigBuilder implements Listener {
	
	private Map<Player, ConfigSelector<?>> currentSelection;
	
	public ConfigBuilder() {
		this.currentSelection = new HashMap<>();
	}
	
	public void setSelector(Player player, ConfigSelector<?> selector) {
		this.currentSelection.put(player, selector); // TODO: Do something with the old selector?
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (this.currentSelection.containsKey(event.getPlayer())) {
			currentSelection.get(event.getPlayer()).onBlockBreak(event);
			
			event.setCancelled(true);
		}
	}
}
