package mcc.config;

import org.bukkit.event.block.BlockBreakEvent;

public interface ConfigSelector<T> {
	
	void onBlockBreak(BlockBreakEvent event);
	
	T build();
	
}