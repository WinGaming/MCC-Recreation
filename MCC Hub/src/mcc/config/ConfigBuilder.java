package mcc.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import mcc.utils.Pair;

public class ConfigBuilder implements Listener {
	
	private Map<Player, ConfigSelector<?>> currentSelection;
	private Map<Player, ConfigAction> currentAction;
	
	public ConfigBuilder() {
		this.currentAction = new HashMap<>();
		this.currentSelection = new HashMap<>();
	}
	
	public void tick() {
		for (Entry<Player, ConfigSelector<?>> selectorPair : this.currentSelection.entrySet()) {
			Player player = selectorPair.getKey();
			selectorPair.getValue().displayTick(player);
			// TODO: Display current selector to player?
		}
	}

	public void setSelector(Player player, ConfigAction action, ConfigSelector<?> selector) {
		if (action == null || selector == null) {
			throw new IllegalArgumentException("Both ConfigAction and ConfigSelector must not be null");
		}

		this.currentSelection.put(player, selector); // TODO: Do something with the old selector?
	}

	public Pair<ConfigAction, ConfigSelector<?>> getCurrentSelection(Player player) {
		return new Pair<>(this.currentAction.get(player), this.currentSelection.get(player));
	}

	public void cancelSelector(Player player) {
		this.currentAction.remove(player);
		this.currentSelection.remove(player);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (this.currentSelection.containsKey(event.getPlayer())) {
			currentSelection.get(event.getPlayer()).onBlockBreak(event);
			
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.cancelSelector(event.getPlayer());
	}
}
