package mcc.yml.hub;

import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Vector3i;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration section for a team box in the
 * Decision-dome implementing {@link MCCConfigSerializable}.
 */
public class TeamBoxConfig implements MCCConfigSerializable {

    private Vector3i cornerA, cornerB;
    private LocationProviderConfig spawnLocationProviderConfig;

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        int ax = config.getInt("corner.a.x");
        int ay = config.getInt("corner.a.y");
        int az = config.getInt("corner.a.z");

        int bx = config.getInt("corner.b.x");
        int by = config.getInt("corner.b.y");
        int bz = config.getInt("corner.b.z");

        this.cornerA = new Vector3i(ax, ay, az);
        this.cornerB = new Vector3i(bx, by, bz);

        LocationProviderConfig locationProviderConfig = new LocationProviderConfig();
        if (!config.contains("spawnprovider")) config.createSection("spawnprovider");
        locationProviderConfig.load(config.getConfigurationSection("spawnprovider"));
        this.spawnLocationProviderConfig = locationProviderConfig;

        return false;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set("corner.a.x", this.cornerA.getX());
        config.set("corner.a.y", this.cornerA.getY());
        config.set("corner.a.z", this.cornerA.getZ());

        config.set("corner.b.x", this.cornerB.getX());
        config.set("corner.b.y", this.cornerB.getY());
        config.set("corner.b.z", this.cornerB.getZ());

        if (!config.contains("spawnprovider")) config.createSection("spawnprovider");
        this.spawnLocationProviderConfig.save(config.getConfigurationSection("spawnprovider"));   
    }

    public void setCornerA(Vector3i cornerA) {
        this.cornerA = cornerA;
    }

    public void setCornerB(Vector3i cornerB) {
        this.cornerB = cornerB;
    }

    public void setSpawnLocationProviderConfig(LocationProviderConfig spawnLocationProviderConfig) {
        this.spawnLocationProviderConfig = spawnLocationProviderConfig;
    }

    public Vector3i getCornerA() {
        return cornerA;
    }

    public Vector3i getCornerB() {
        return cornerB;
    }

    public LocationProviderConfig getSpawnLocationProviderConfig() {
        return spawnLocationProviderConfig;
    }
}
