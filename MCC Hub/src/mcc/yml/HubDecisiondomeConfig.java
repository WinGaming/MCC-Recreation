package mcc.yml;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import mcc.config.LocationListSelector;
import mcc.utils.Pair;
import mcc.utils.Vector3i;

/**
 * This class represents the configuration section for the Decision-dome implementing {@link MCCConfigSerializable}.
 */
public class HubDecisiondomeConfig implements MCCConfigSerializable {
	
	private String worldName = "WORLD_NAME";
	
	private HubDecisiondomeSingleFieldConfig[] fields = new HubDecisiondomeSingleFieldConfig[0];
	
	private Pair<TimeUnit, Integer> gameSelectionTimer = new Pair<>(TimeUnit.SECONDS, 30);
	private Pair<TimeUnit, Integer> gameSelectionFinalTimer = new Pair<>(TimeUnit.SECONDS, 5);
	private Pair<TimeUnit, Integer> gameSelectedTimer = new Pair<>(TimeUnit.SECONDS, 10);
	private Pair<TimeUnit, Integer> gameSelectedAwaitTeleportTimer = new Pair<>(TimeUnit.HOURS, 10);
	
	private int minTickDelay = 2;
	private int maxAdditionalTickDelay = 20;
	
	private HubDecisiondomeFieldTypeConfig enabledState = new HubDecisiondomeFieldTypeConfig(Material.WHITE_WOOL);
	private HubDecisiondomeFieldTypeConfig disabledState = new HubDecisiondomeFieldTypeConfig(Material.RED_WOOL);
	private HubDecisiondomeFieldTypeConfig highlightedState = new HubDecisiondomeFieldTypeConfig(Material.LIME_WOOL);
	private HubDecisiondomeFieldTypeConfig selectedState = new HubDecisiondomeFieldTypeConfig(Material.GREEN_WOOL);
	
	/**
	 * Loads and returns the stored timer values and a {@link Boolean} if default values were used.
	 * The data is loaded under the key <code>"timer." + timerKey</code>
	 * @param timerKey is the key of the name of the timer
	 * @param defaultUnit is the default {@link TimeUnit}
	 * @param defaultAmount is the default amount of time
	 * @param config the {@link ConfigurationSection} to load from
	 * @return the stored timer values and a {@link Boolean} if default values were used
	 */
	private static Pair<Pair<TimeUnit, Integer>, Boolean> loadTimerPair(String timerKey, TimeUnit defaultUnit, int defaultAmount, ConfigurationSection config) {
		String unitKey = "timer." + timerKey + ".unit";
		String amountKey = "timer." + timerKey + ".amount";
		
		Pair<TimeUnit, Boolean> gameSelectionUnit = ConfigUtils.readEnumValue(TimeUnit.class, unitKey, defaultUnit, config);
		if (config.contains(amountKey)) {
			int gameSelectionAmount = config.getInt(amountKey);
			return new Pair<>(new Pair<>(gameSelectionUnit.getA(), gameSelectionAmount), gameSelectionUnit.getB());
		} else {
			return new Pair<>(new Pair<>(gameSelectionUnit.getA(), defaultAmount), true);
		}
	}
	
	@Override
	public boolean load(ConfigurationSection config) throws IllegalArgumentException {
		boolean valuesChanged = false;
		
		// Fields
		config.createSection("fields");
		Set<String> stringFields = config.getConfigurationSection("fields").getKeys(false);
		HubDecisiondomeSingleFieldConfig[] newFields = new HubDecisiondomeSingleFieldConfig[stringFields.size()];

		int i = 0;
		for (String key : stringFields) {
			HubDecisiondomeSingleFieldConfig newConfig = new HubDecisiondomeSingleFieldConfig();
			config.createSection("fields." + key);
			newConfig.load(config.getConfigurationSection("fields." + key));
			newFields[i] = newConfig;
			i++;
		}
		this.fields = newFields;
		
		// Timers
		Pair<Pair<TimeUnit, Integer>, Boolean> gameSelection = loadTimerPair("selection", TimeUnit.SECONDS, 30, config);
		this.gameSelectionTimer = gameSelection.getA();
		
		Pair<Pair<TimeUnit, Integer>, Boolean> gameSelectionFinal = loadTimerPair("selection-final", TimeUnit.SECONDS, 5, config);
		this.gameSelectionFinalTimer = gameSelectionFinal.getA();
		
		Pair<Pair<TimeUnit, Integer>, Boolean> gameSelected = loadTimerPair("selected", TimeUnit.SECONDS, 10, config);
		this.gameSelectedTimer = gameSelected.getA();
		
		Pair<Pair<TimeUnit, Integer>, Boolean> gameSelectedAwaitTeleport = loadTimerPair("await-teleport", TimeUnit.HOURS, 10, config);
		this.gameSelectedAwaitTeleportTimer = gameSelectedAwaitTeleport.getA();
		
		valuesChanged = valuesChanged || gameSelection.getB() || gameSelectionFinal.getB() || gameSelected.getB() || gameSelectedAwaitTeleport.getB();
		
		if (config.contains("min-delay")) this.minTickDelay = config.getInt("min-delay");
		else { this.minTickDelay = 2; valuesChanged = true; }
		
		if (config.contains("max-additional-delay")) this.maxAdditionalTickDelay = config.getInt("max-additional-delay");
		else { this.maxAdditionalTickDelay = 20; valuesChanged = true; }
		
		// Field States
		config.createSection("states.enabled");
		config.createSection("states.disabled");
		config.createSection("states.highlighted");
		config.createSection("states.selected");
		valuesChanged = valuesChanged || this.enabledState.load(config.getConfigurationSection("states.enabled"));
		valuesChanged = valuesChanged || this.disabledState.load(config.getConfigurationSection("states.disabled"));
		valuesChanged = valuesChanged || this.highlightedState.load(config.getConfigurationSection("states.highlighted"));
		valuesChanged = valuesChanged || this.selectedState.load(config.getConfigurationSection("states.selected"));
		
		// World
		if (config.contains("world")) { this.worldName = config.getString("world"); }
		else { this.worldName = "WORLD_NAME"; valuesChanged = true; }
		
		return valuesChanged;
	}
	
	@Override
	public void save(ConfigurationSection config) {
		// Fields
		for (int i = 0; i < this.fields.length; i++) {
			HubDecisiondomeSingleFieldConfig fieldConfig = this.fields[i];
			config.createSection("fields." + i);
			fieldConfig.save(config.getConfigurationSection("fields." + i));
		}
		
		// Timers
		config.set("timer.selection.unit", gameSelectionTimer.getA().name());
		config.set("timer.selection.amount", gameSelectionTimer.getB());
		
		config.set("timer.selection-final.unit", gameSelectionFinalTimer.getA().name());
		config.set("timer.selection-final.amount", gameSelectionFinalTimer.getB());
		
		config.set("timer.selected.unit", gameSelectedTimer.getA().name());
		config.set("timer.selected.amount", gameSelectedTimer.getB());
		
		config.set("timer.await-teleport.unit", gameSelectedAwaitTeleportTimer.getA().name());
		config.set("timer.await-teleport.amount", gameSelectedAwaitTeleportTimer.getB());
		
		// Delays
		config.set("min-delay", this.minTickDelay);
		config.set("max-additional-delay", this.maxAdditionalTickDelay);
		
		// Field States
		config.createSection("states.enabled");
		config.createSection("states.disabled");
		config.createSection("states.highlighted");
		config.createSection("states.selected");
		this.enabledState.save(config.getConfigurationSection("states.enabled"));
		this.disabledState.save(config.getConfigurationSection("states.disabled"));
		this.highlightedState.save(config.getConfigurationSection("states.highlighted"));
		this.selectedState.save(config.getConfigurationSection("states.selected"));
		
		// World
		config.set("world", this.worldName);
	}
	
	// Getters
	public Pair<TimeUnit, Integer> getGameSelectionTimer() {
		return gameSelectionTimer;
	}
	
	public Pair<TimeUnit, Integer> getGameSelectionFinalTimer() {
		return gameSelectionFinalTimer;
	}
	
	public Pair<TimeUnit, Integer> getGameSelectedTimer() {
		return gameSelectedTimer;
	}
	
	public Pair<TimeUnit, Integer> getGameSelectedAwaitTeleportTimer() {
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
	
	public String getWorldName() {
		return worldName;
	}
	
	// Setters
	public Optional<String> addFieldFromSelector(LocationListSelector selector) {
		List<String> locationIndexList = new ArrayList<>();
		
		List<Vector3i> positionList = new ArrayList<>();
		for (Location location : selector.build()) {
			if (!this.worldName.equals(location.getWorld().getName())) {
				return Optional.of("At least one block was placed in a diffrent world");
			}
			
			if (!locationIndexList.contains(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
				positionList.add(new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
			}
		}
		
		Vector3i[] positions = positionList.toArray(new Vector3i[positionList.size()]);
		HubDecisiondomeSingleFieldConfig fieldConfig = new HubDecisiondomeSingleFieldConfig();
		fieldConfig.positions = positions;
		
		HubDecisiondomeSingleFieldConfig[] newFields = new HubDecisiondomeSingleFieldConfig[this.fields.length + 1];
		for (int i = 0; i < this.fields.length; i++) {
			newFields[i] = this.fields[i];
		}
		
		newFields[newFields.length - 1] = fieldConfig;
		
		return Optional.empty();
	}
	
	/**
	 * This class represents the configuration section for field-type in the Decision-dome implementing {@link MCCConfigSerializable}.
	 */
	public class HubDecisiondomeFieldTypeConfig implements MCCConfigSerializable {
		
		private final Material defaultMaterial;
		
		private Material material;
		
		public HubDecisiondomeFieldTypeConfig() { this.defaultMaterial = Material.ORANGE_WOOL; this.material = defaultMaterial; }
		public HubDecisiondomeFieldTypeConfig(Material defaultMaterial) { this.defaultMaterial = defaultMaterial; this.material = defaultMaterial; }
		
		
		@Override
		public boolean load(ConfigurationSection config) throws IllegalArgumentException {
			Pair<Material, Boolean> value = ConfigUtils.readEnumValue(Material.class, "material", this.defaultMaterial, config);
			this.material = value.getA();
			return value.getB();
		}
		
		@Override
		public void save(ConfigurationSection config) {
			config.set("material", this.material.name());
		}
		
		public Material getMaterial() {
			return material;
		}
	}
	
	/**
	 * This class represents the configuration section for a single field in the Decision-dome implementing {@link MCCConfigSerializable}.
	 */
	public class HubDecisiondomeSingleFieldConfig implements MCCConfigSerializable {
		
		private Vector3i[] positions = new Vector3i[0];

		@Override
		public boolean load(ConfigurationSection config) {
			Set<String> stringPositions = config.getConfigurationSection("positions").getKeys(false);
			Vector3i[] newPositions = new Vector3i[stringPositions.size()];

			int i = 0;
			for (String key : stringPositions) {
				int x = config.getInt("positions." + key + ".x");
				int y = config.getInt("positions." + key + ".y");
				int z = config.getInt("positions." + key + ".z");
				newPositions[i] = new Vector3i(x, y, z);
				
				i++;
			}
			
			this.positions = newPositions;
			return false;
		}
		
		@Override
		public void save(ConfigurationSection config) {
			config.set("positions", null); // Make sure all old positions are deleted
			
			for (int i = 0; i < this.positions.length; i++) {
				config.set("positions." + i + ".x", positions[i].getX());
				config.set("positions." + i + ".y", positions[i].getY());
				config.set("positions." + i + ".z", positions[i].getZ());
			}
		}
		
		public Vector3i[] getPositions() {
			return positions;
		}
	}
}
