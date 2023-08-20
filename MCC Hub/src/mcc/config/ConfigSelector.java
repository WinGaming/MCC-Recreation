package mcc.config;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public interface ConfigSelector<T> {
	
	default void init() {}
	 
	default void onBlockBreak(BlockBreakEvent event) {};
	default void onSneakChange(PlayerToggleSneakEvent event) {};
	
	T build();

	void displayTick(Player player);
	
	default boolean nextStep() {
		return false;
	}
}