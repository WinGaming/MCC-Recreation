package mcc.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import mcc.indicator.ParticleIndicator;

public class LocationListSelector implements ConfigSelector<List<Location>> {

	private List<Location> list;
	
	public LocationListSelector() {
		this.list = new ArrayList<>();
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (list.contains(event.getBlock().getLocation())) {
			list.remove(event.getBlock().getLocation());
		} else {
			list.add(event.getBlock().getLocation());
		}
	}

	@Override
	public void displayTick(Player player) {
		ParticleIndicator.highlightBlocks(player.getWorld(), this.build());
	}

	@Override
	public List<Location> build() {
		return this.list; // TODO: Needs a copy?
	}
}
