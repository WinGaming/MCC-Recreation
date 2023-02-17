package mcc.yml;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Pair;
import mcc.utils.Vector3i;

public class HubDecisiondomeConfig {
	
	private HubDecisiondomeSingleFieldConfig[] fields;
	
	private Pair<TimeUnit, Integer> gameSelectionTimer;
	private Pair<TimeUnit, Integer> gameSelectionFinalTimer;
	private Pair<TimeUnit, Integer> gameSelectedTimer;
	private Pair<TimeUnit, Integer> gameSelectedAwaitTeleportTimer;
	
	private int minTickDelay;
	private int maxAdditionalTickDelay;
	
	private HubDecisiondomeFieldTypeConfig enabledState;
	private HubDecisiondomeFieldTypeConfig disabledState;
	private HubDecisiondomeFieldTypeConfig highlighedState;
	private HubDecisiondomeFieldTypeConfig selectedState;
	
	public class HubDecisiondomeFieldTypeConfig implements MCCConfigSerializable {
		
		private Material material;
		
		@Override
		public boolean load(ConfigurationSection config) throws IllegalArgumentException {
			if (config.contains("material")) {
				String materialName = config.getString("material");
				this.material = Material.valueOf(materialName);
				return false;
			} else {
				this.material = Material.ORANGE_WOOL;
				return true;
			}
		}
		
		@Override
		public void save(ConfigurationSection config) {
			config.set("material", this.material.name());
		}
		
		public Material getMaterial() {
			return material;
		}
	}
	
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
