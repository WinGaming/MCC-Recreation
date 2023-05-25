package mcc.config;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface ConfigSelector<T> {
	
	default void init() {}
	 
	default void onBlockBreak(BlockBreakEvent event) {};
	
	T build();

	void displayTick(Player player);
	
}