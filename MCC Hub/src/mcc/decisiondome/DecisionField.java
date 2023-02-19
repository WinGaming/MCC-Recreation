package mcc.decisiondome;

import org.bukkit.Location;

import mcc.yml.HubDecisiondomeConfig;

public class DecisionField {

	private Location[] blockLocations;
	
	private DecisionFieldState state;
	
	private boolean dirty = true;
	
	private HubDecisiondomeConfig decisiondomeConfig;
	
	public DecisionField(Location[] blockLocations, DecisionFieldState state, HubDecisiondomeConfig config) {
		this.blockLocations = blockLocations;
		this.state = state;
		
		this.decisiondomeConfig = config;
	}
	
	public void tick() {
		if (this.dirty) {
			for (Location location : this.blockLocations) {
				switch (this.state) {
				case ENABLED: location.getBlock().setType(this.decisiondomeConfig.getEnabledState().getMaterial()); break;
				case DISABLED: location.getBlock().setType(this.decisiondomeConfig.getDisabledState().getMaterial()); break;
				case HIGHLIGHTED: location.getBlock().setType(this.decisiondomeConfig.getHighlightedState().getMaterial()); break;
				case SELECTED: location.getBlock().setType(this.decisiondomeConfig.getSelectedState().getMaterial()); break;
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
