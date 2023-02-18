package mcc.yml;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Pair;
import mcc.utils.Vector3i;

/**
 * This class represents the configuration section for the Decision-dome implementing {@link MCCConfigSerializable}.
 */
public class HubDecisiondomeConfig implements MCCConfigSerializable {
	
	private HubDecisiondomeSingleFieldConfig[] fields = new HubDecisiondomeSingleFieldConfig[0];
	
	private Pair<TimeUnit, Integer> gameSelectionTimer = new Pair<>(TimeUnit.SECONDS, 30);
	private Pair<TimeUnit, Integer> gameSelectionFinalTimer = new Pair<>(TimeUnit.SECONDS, 5);
	private Pair<TimeUnit, Integer> gameSelectedTimer = new Pair<>(TimeUnit.SECONDS, 10);
	private Pair<TimeUnit, Integer> gameSelectedAwaitTeleportTimer = new Pair<>(TimeUnit.HOURS, 10);
	
	private int minTickDelay = 2;
	private int maxAdditionalTickDelay = 20;
	
	private HubDecisiondomeFieldTypeConfig enabledState = new HubDecisiondomeFieldTypeConfig(Material.WHITE_WOOL);
	private HubDecisiondomeFieldTypeConfig disabledState = new HubDecisiondomeFieldTypeConfig(Material.LIGHT_GRAY_WOOL);
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
		Set<String> stringFields = config.getConfigurationSection("fields").getKeys(false);
		HubDecisiondomeSingleFieldConfig[] newFields = new HubDecisiondomeSingleFieldConfig[stringFields.size()];

		int i = 0;
		for (String key : stringFields) {
			HubDecisiondomeSingleFieldConfig newConfig = new HubDecisiondomeSingleFieldConfig();
			newConfig.load(config.getConfigurationSection("fields." + key));
			newFields[i] = newConfig;
			i++;
		}
		this.fields = newFields;
		
		// Timers
		var gameSelection = loadTimerPair("gameselection", TimeUnit.SECONDS, 30, config);
		this.gameSelectionTimer = gameSelection.getA();
		
		var gameSelectionFinal = loadTimerPair("gameselectionfinal", TimeUnit.SECONDS, 5, config);
		this.gameSelectionFinalTimer = gameSelectionFinal.getA();
		
		var gameSelected = loadTimerPair("gameselected", TimeUnit.SECONDS, 10, config);
		this.gameSelectedTimer = gameSelected.getA();
		
		var gameSelectedAwaitTeleport = loadTimerPair("awaitteleport", TimeUnit.HOURS, 10, config);
		this.gameSelectedAwaitTeleportTimer = gameSelectedAwaitTeleport.getA();
		
		valuesChanged = valuesChanged || gameSelection.getB() || gameSelectionFinal.getB() || gameSelected.getB() || gameSelectedAwaitTeleport.getB();
		
		if (config.contains("min-delay")) this.minTickDelay = config.getInt("min-delay");
		else { this.minTickDelay = 2; valuesChanged = true; }
		
		if (config.contains("max-additional-delay")) this.maxAdditionalTickDelay = config.getInt("max-additional-delay");
		else { this.maxAdditionalTickDelay = 20; valuesChanged = true; }
		
		// Field States
		valuesChanged = valuesChanged || this.enabledState.load(config.getConfigurationSection("states.enabled"));
		valuesChanged = valuesChanged || this.disabledState.load(config.getConfigurationSection("states.disabled"));
		valuesChanged = valuesChanged || this.highlightedState.load(config.getConfigurationSection("states.highlighted"));
		valuesChanged = valuesChanged || this.selectedState.load(config.getConfigurationSection("state.selected"));
		
		return valuesChanged;
	}
	
	@Override
	public void save(ConfigurationSection config) {
		// Fields
		for (int i = 0; i < this.fields.length; i++) {
			HubDecisiondomeSingleFieldConfig fieldConfig = this.fields[i];
			fieldConfig.save(config.getConfigurationSection("fields." + i));
		}
		
		// Timers
		config.set("timer.gameselection.unit", gameSelectionTimer.getA().name());
		config.set("timer.gameselection.amount", gameSelectionTimer.getB());
		
		config.set("timer.gameselectionfinal.unit", gameSelectionFinalTimer.getA().name());
		config.set("timer.gameselectionfinal.amount", gameSelectionFinalTimer.getB());
		
		config.set("timer.gameselected.unit", gameSelectedTimer.getA().name());
		config.set("timer.gameselected.amount", gameSelectedTimer.getB());
		
		config.set("timer.awaitteleport.unit", gameSelectedAwaitTeleportTimer.getA().name());
		config.set("timer.awaitteleport.amount", gameSelectedAwaitTeleportTimer.getB());
		
		// Delays
		config.set("min-delay", this.minTickDelay);
		config.set("max-additional-delay", this.maxAdditionalTickDelay);
		
		// Field States
		this.enabledState.save(config.getConfigurationSection("states.enabled"));
		this.disabledState.save(config.getConfigurationSection("states.disabled"));
		this.highlightedState.save(config.getConfigurationSection("states.highlighted"));
		this.selectedState.save(config.getConfigurationSection("state.selected"));
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
