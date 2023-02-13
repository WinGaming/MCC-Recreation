package mcc.decisiondome;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

public class DecisionField {

	private List<Location> blockLocations;
	
	private DecisionFieldState state;
	
	private boolean dirty = true;
	
	public DecisionField(List<Location> blockLocations, DecisionFieldState state) {
		this.blockLocations = blockLocations;
		this.state = state;
	}
	
	public void tick() {
		if (this.dirty) {
			for (Location location : this.blockLocations) {
				switch (this.state) {
				case ENABLED: location.getBlock().setType(Material.WHITE_WOOL); break;
				case DISABLED: location.getBlock().setType(Material.RED_WOOL); break;
				case HIGHLIGHTED: location.getBlock().setType(Material.LIME_WOOL); break;
				case SELECTED: location.getBlock().setType(Material.GREEN_WOOL); break;
				}
			}
		}
	}
	
	public void setState(DecisionFieldState state) {
		this.dirty = this.state != state;
		this.state = state;
	}

	public enum DecisionFieldState {
		ENABLED, HIGHLIGHTED, DISABLED, SELECTED
	}
}
