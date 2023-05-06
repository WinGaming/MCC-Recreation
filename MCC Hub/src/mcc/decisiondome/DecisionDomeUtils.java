package mcc.decisiondome;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.utils.Vector3i;
import mcc.yml.hub.HubDecisiondomeConfig;

public class DecisionDomeUtils {
    
	public static DecisionDome loadFromConfig(HubDecisiondomeConfig config) throws IllegalArgumentException {
		World world = Bukkit.getWorld(config.getWorldName());
		
		if (world == null) {
			throw new IllegalArgumentException("Failed to reload: Could not find world \"" + config.getWorldName() + "\"");
		}
		
		DecisionField[] fields = new DecisionField[config.getFields().length];
		for (int i = 0; i < fields.length; i++) {
			Vector3i[] positions = config.getFields()[i].getPositions();
			Location[] locations = new Location[positions.length];
			
			for (int j = 0; j < positions.length; j++) {
				locations[j] = new Location(world, positions[j].getX(), positions[j].getY(), positions[j].getZ());
			}
			
			fields[i] = new DecisionField(locations, DecisionFieldState.ENABLED, config);
		}
		
		return new DecisionDome(fields, config);
	}
}
