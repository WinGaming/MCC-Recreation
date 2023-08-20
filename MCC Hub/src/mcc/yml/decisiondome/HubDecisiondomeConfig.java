package mcc.yml.decisiondome;

import static mcc.yml.decisiondome.LocationUtils.toVector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import mcc.config.LocationListSelector;
import mcc.config.TeamBoxSelector;
import mcc.locationprovider.StaticLocationProvider;
import mcc.utils.Vector2f;
import mcc.utils.Vector3d;
import mcc.utils.Vector3i;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration section for the Decision-dome implementing {@link MCCConfigSerializable}.
 */
public class HubDecisiondomeConfig implements MCCConfigSerializable {
	
	private String worldName = "WORLD_NAME";
	
	private HubDecisiondomeSingleFieldConfig[] fields = new HubDecisiondomeSingleFieldConfig[0];
	
	private TimerConfig gameSelectionPreVoteTimer = new TimerConfig(TimeUnit.SECONDS, 60);
	private TimerConfig gameSelectionTimer = new TimerConfig(TimeUnit.SECONDS, 30);
	private TimerConfig gameSelectionFinalTimer = new TimerConfig(TimeUnit.SECONDS, 5, 10);
	private TimerConfig gameSelectedAwaitTeleportTimer = new TimerConfig(TimeUnit.HOURS, 10);
	
	private int minTickDelay = 2;
	private int maxAdditionalTickDelay = 20;
	
	private HubDecisiondomeFieldTypeConfig enabledState = new HubDecisiondomeFieldTypeConfig(Material.WHITE_WOOL);
	private HubDecisiondomeFieldTypeConfig disabledState = new HubDecisiondomeFieldTypeConfig(Material.RED_WOOL);
	private HubDecisiondomeFieldTypeConfig highlightedState = new HubDecisiondomeFieldTypeConfig(Material.LIME_WOOL);
	private HubDecisiondomeFieldTypeConfig selectedState = new HubDecisiondomeFieldTypeConfig(Material.GREEN_WOOL);
	
	private TeamBoxConfig[] teamBoxes = new TeamBoxConfig[0];
	
	@Override
	public boolean load(ConfigurationSection config) throws IllegalArgumentException {
		boolean valuesChanged = false;

		// World
		if (config.contains("world")) { this.worldName = config.getString("world"); }
		else { this.worldName = "WORLD_NAME"; valuesChanged = true; }

		// Fields
		if (!config.contains("fields")) config.createSection("fields");
		Set<String> stringFields = config.getConfigurationSection("fields").getKeys(false);
		HubDecisiondomeSingleFieldConfig[] newFields = new HubDecisiondomeSingleFieldConfig[stringFields.size()];

		int i = 0;
		for (String key : stringFields) {
			HubDecisiondomeSingleFieldConfig newConfig = new HubDecisiondomeSingleFieldConfig();
			if (!config.contains("fields." + key)) config.createSection("fields." + key);
			newConfig.load(config.getConfigurationSection("fields." + key));
			newFields[i] = newConfig;
			i++;
		}
		this.fields = newFields;

		// Team boxes
		if (!config.contains("teamboxes")) config.createSection("teamboxes");
		Set<String> stringBoxes = config.getConfigurationSection("teamboxes").getKeys(false);
		TeamBoxConfig[] newBoxes = new TeamBoxConfig[stringBoxes.size()];

		int j = 0;
		for (String key : stringBoxes) {
			TeamBoxConfig newConfig = new TeamBoxConfig();
			if (!config.contains("teamboxes." + key)) config.createSection("teamboxes." + key);
			newConfig.load(config.getConfigurationSection("teamboxes." + key));
			newBoxes[j] = newConfig;
			j++;
		}
		this.teamBoxes = newBoxes;
		
		// Timers
		if (!config.contains("timer")) config.createSection("timer");

		if (!config.contains("timer.intro")) config.createSection("timer.intro");
		valuesChanged = valuesChanged || this.gameSelectionPreVoteTimer.load(config.getConfigurationSection("timer.intro"));

		if (!config.contains("timer.selection")) config.createSection("timer.selection");
		valuesChanged = valuesChanged || this.gameSelectionTimer.load(config.getConfigurationSection("timer.selection"));
		
		if (!config.contains("timer.selection-final")) config.createSection("timer.selection-final");
		valuesChanged = valuesChanged || this.gameSelectionFinalTimer.load(config.getConfigurationSection("timer.selection-final"));
		
		if (!config.contains("timer.await-teleport")) config.createSection("timer.await-teleport");
		valuesChanged = valuesChanged || this.gameSelectedAwaitTeleportTimer.load(config.getConfigurationSection("timer.await-teleport"));
		
		if (config.contains("min-delay")) this.minTickDelay = config.getInt("min-delay");
		else { this.minTickDelay = 2; valuesChanged = true; }
		
		if (config.contains("max-additional-delay")) this.maxAdditionalTickDelay = config.getInt("max-additional-delay");
		else { this.maxAdditionalTickDelay = 20; valuesChanged = true; }
		
		// Field States
		if (!config.contains("states.enabled")) config.createSection("states.enabled");
		if (!config.contains("states.disabled")) config.createSection("states.disabled");
		if (!config.contains("states.highlighted")) config.createSection("states.highlighted");
		if (!config.contains("states.selected")) config.createSection("states.selected");
		valuesChanged = valuesChanged || this.enabledState.load(config.getConfigurationSection("states.enabled"));
		valuesChanged = valuesChanged || this.disabledState.load(config.getConfigurationSection("states.disabled"));
		valuesChanged = valuesChanged || this.highlightedState.load(config.getConfigurationSection("states.highlighted"));
		valuesChanged = valuesChanged || this.selectedState.load(config.getConfigurationSection("states.selected"));
		
		return valuesChanged;
	}
	
	@Override
	public void save(ConfigurationSection config) {
		// Fields
		for (int i = 0; i < this.fields.length; i++) {
			HubDecisiondomeSingleFieldConfig fieldConfig = this.fields[i];
			if (!config.contains("fields." + i)) config.createSection("fields." + i);
			fieldConfig.save(config.getConfigurationSection("fields." + i));
		}

		// Team boxes
		for (int i = 0; i < this.teamBoxes.length; i++) {
			TeamBoxConfig boxConfig = this.teamBoxes[i];
			if (!config.contains("teamboxes." + i)) config.createSection("teamboxes." + i);
			boxConfig.save(config.getConfigurationSection("teamboxes." + i));
		}
		
		// Timers
		if (!config.contains("timer")) config.createSection("timer");

		if (!config.contains("timer.intro")) config.createSection("timer.intro");
		gameSelectionPreVoteTimer.save(config.getConfigurationSection("timer.intro"));

		if (!config.contains("timer.selection")) config.createSection("timer.selection");
		gameSelectionTimer.save(config.getConfigurationSection("timer.selection"));

		if (!config.contains("timer.selection-final")) config.createSection("timer.selection-final");
		gameSelectionFinalTimer.save(config.getConfigurationSection("timer.selection-final"));

		if (!config.contains("timer.await-teleport")) config.createSection("timer.await-teleport");
		gameSelectedAwaitTeleportTimer.save(config.getConfigurationSection("timer.await-teleport"));
		
		// Delays
		config.set("min-delay", this.minTickDelay);
		config.set("max-additional-delay", this.maxAdditionalTickDelay);
		
		// Field States
		if (!config.contains("states.enabled")) config.createSection("states.enabled");
		if (!config.contains("states.disabled")) config.createSection("states.disabled");
		if (!config.contains("states.highlighted")) config.createSection("states.highlighted");
		if (!config.contains("states.selected")) config.createSection("states.selected");
		this.enabledState.save(config.getConfigurationSection("states.enabled"));
		this.disabledState.save(config.getConfigurationSection("states.disabled"));
		this.highlightedState.save(config.getConfigurationSection("states.highlighted"));
		this.selectedState.save(config.getConfigurationSection("states.selected"));
		
		// World
		config.set("world", this.worldName);
	}
	
	// Getters
	public TimerConfig getGameSelectionPreVoteTimer() {
		return gameSelectionPreVoteTimer;
	}

	public TimerConfig getGameSelectionTimer() {
		return gameSelectionTimer;
	}
	
	public TimerConfig getGameSelectionFinalTimer() {
		return gameSelectionFinalTimer;
	}
	
	public TimerConfig getGameSelectedAwaitTeleportTimer() {
		return gameSelectedAwaitTeleportTimer;
	}
	
	public int getMinTickDelay() {
		return minTickDelay;
	}
	
	public int getMaxAdditionalTickDelay() {
		return maxAdditionalTickDelay;
	}
	
	public HubDecisiondomeFieldTypeConfig getEnabledState() {
		return enabledState;
	}
	
	public HubDecisiondomeFieldTypeConfig getDisabledState() {
		return disabledState;
	}
	
	public HubDecisiondomeFieldTypeConfig getHighlightedState() {
		return highlightedState;
	}
	
	public HubDecisiondomeFieldTypeConfig getSelectedState() {
		return selectedState;
	}
	
	public HubDecisiondomeSingleFieldConfig[] getFields() {
		return fields;
	}

	public TeamBoxConfig[] getTeamBoxes() {
		return teamBoxes;
	}
	
	public String getWorldName() {
		return worldName;
	}
	
	// Setters
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public Optional<String> addFieldFromSelector(LocationListSelector selector) {
		List<String> locationIndexList = new ArrayList<>();
		
		List<Vector3i> positionList = new ArrayList<>();
		for (Location location : selector.build()) {
			if (!this.worldName.equals(location.getWorld().getName())) {
				return Optional.of("At least one block was placed in a diffrent world than the decisiondome");
			}
			
			if (!locationIndexList.contains(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
				positionList.add(new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
			}
		}
		
		Vector3i[] positions = positionList.toArray(new Vector3i[positionList.size()]);
		HubDecisiondomeSingleFieldConfig fieldConfig = new HubDecisiondomeSingleFieldConfig();
		fieldConfig.setPositions(positions);
		
		HubDecisiondomeSingleFieldConfig[] newFields = new HubDecisiondomeSingleFieldConfig[this.fields.length + 1];
		for (int i = 0; i < this.fields.length; i++) {
			newFields[i] = this.fields[i];
		}
		
		newFields[newFields.length - 1] = fieldConfig;
		this.fields = newFields;
		
		return Optional.empty();
	}

	public Optional<String> addTeamboxFromSelector(TeamBoxSelector selector) {
		var selectorResult = selector.build();

		if (
			!this.worldName.equals(selectorResult.getA().getA().getWorld().getName()) ||
			!this.worldName.equals(selectorResult.getA().getB().getWorld().getName()) ||
			!this.worldName.equals(selectorResult.getB().getWorld().getName())
		) {
			return Optional.of("At least one corner is in a diffrent world than the decisiondome");
		}

		if (selectorResult.getA() == null || selectorResult.getB() == null) {
			return Optional.of("At least one corner is not selected");
		}

		TeamBoxConfig teamboxConfig = new TeamBoxConfig();
		teamboxConfig.setCornerA(toVector3i(selectorResult.getA().getA()));
		teamboxConfig.setCornerB(toVector3i(selectorResult.getA().getB()));

		var locationProviderConfig = new LocationProviderConfig();
		Location loc = selectorResult.getB();
		Vector3d posVec = new Vector3d(loc.getX(), loc.getY(), loc.getZ());
		Vector2f yawPitch = new Vector2f(loc.getYaw(), loc.getPitch());
		locationProviderConfig.setProvider(new StaticLocationProvider(posVec, yawPitch));
		teamboxConfig.setSpawnLocationProviderConfig(locationProviderConfig);
		
		TeamBoxConfig[] newBoxes = new TeamBoxConfig[this.teamBoxes.length + 1];
		for (int i = 0; i < this.teamBoxes.length; i++) {
			newBoxes[i] = this.teamBoxes[i];
		}
		
		newBoxes[newBoxes.length - 1] = teamboxConfig;
		this.teamBoxes = newBoxes;
		
		return Optional.empty();
	}
}
