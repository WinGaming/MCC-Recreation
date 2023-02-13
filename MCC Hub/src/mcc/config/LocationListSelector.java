package mcc.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

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
	public List<Location> build() {
		return this.list; // TODO: Needs a copy?
	}
}
