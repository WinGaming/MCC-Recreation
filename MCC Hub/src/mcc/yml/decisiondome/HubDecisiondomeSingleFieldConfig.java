package mcc.yml.decisiondome;

import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Vector3i;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration section for a single field in the
 * Decision-dome implementing {@link MCCConfigSerializable}.
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

    public void setPositions(Vector3i[] positions) {
        this.positions = positions;
    }

    public Vector3i[] getPositions() {
        return positions;
    }
}
