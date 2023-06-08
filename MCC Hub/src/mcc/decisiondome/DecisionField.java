package mcc.decisiondome;

import org.bukkit.Location;

import mcc.ExampleDecisionFieldDisplay;
import mcc.decisiondome.display.DecisionFieldDisplay;
import mcc.yml.decisiondome.HubDecisiondomeConfig;

public class DecisionField {

	private final Location[] blockLocations;
	private final DecisionFieldDisplay display;
	private final HubDecisiondomeConfig decisiondomeConfig;
	
	private boolean dirty = true;
	private DecisionFieldState state;

	private String gameKey = null;
	
	public DecisionField(Location[] blockLocations, DecisionFieldState state, HubDecisiondomeConfig config) {
		this.blockLocations = blockLocations;
		this.decisiondomeConfig = config;
		
		this.state = state;

		this.display = new ExampleDecisionFieldDisplay(this);
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
		this.display.displayGame(this.gameKey);
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
		/** The field is active and could be chosen */
		ENABLED,
		/** The field is highlighted, but not cosen */
		HIGHLIGHTED,
		/** The field is disabled and can not be used */
		DISABLED,
		/** The field is selected and should not change this round */
		SELECTED
	}
}
