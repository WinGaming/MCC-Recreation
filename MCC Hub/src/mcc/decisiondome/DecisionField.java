package mcc.decisiondome;

import org.bukkit.Location;

import mcc.yml.decisiondome.HubDecisiondomeConfig;

public class DecisionField {

	private final Location[] blockLocations;
	private final DecisionFieldDisplay display;
	private final HubDecisiondomeConfig decisiondomeConfig;
	
	private boolean dirty = true;
	private DecisionFieldState state;

	private String gameKey = null;
	
	public DecisionField(Location[] blockLocations, DecisionFieldState state, HubDecisiondomeConfig config, DecisionFieldDisplay display) {
		this.blockLocations = blockLocations;
		this.decisiondomeConfig = config;
		
		this.state = state;

		this.display = display;
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

	public boolean hasGameKey() {
		return this.gameKey == null;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}

	public String getGameKey() {
		return gameKey;
	}

	public boolean isDisabled() {
		return this.state == DecisionFieldState.DISABLED;
	}

	public Location[] getBlockLocations() {
		return blockLocations;
	}
	
	public void setState(DecisionFieldState state) {
		this.dirty = this.state != state;
		this.state = state;
	}

	public DecisionFieldDisplay getDisplay() {
		return display;
	}

	public enum DecisionFieldState {
		ENABLED, HIGHLIGHTED, DISABLED, SELECTED
	}
}
