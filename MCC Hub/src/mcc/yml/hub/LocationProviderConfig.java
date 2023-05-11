package mcc.yml.hub;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import mcc.locationprovider.LocationProvider;
import mcc.locationprovider.StaticLocationProvider;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration section for a location provider
 * implementing {@link MCCConfigSerializable}.
 */
public class LocationProviderConfig implements MCCConfigSerializable {

    private World world;

    private LocationProvider provider;

    public LocationProviderConfig(World world) {
        this.world = world;
    }

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        String type = config.getString("type");

        if (type.equalsIgnoreCase("static")) {
            double x = config.getDouble("config.location.x");
            double y = config.getDouble("config.location.y");
            double z = config.getDouble("config.location.z");

            this.provider = new StaticLocationProvider(new Location(this.world, x, y, z));
        } else {
            throw new IllegalArgumentException("Unknown location-provider type in config: \"" + type + "\"");
        }

        return false;
    }

    @Override
    public void save(ConfigurationSection config) {
        if (provider instanceof StaticLocationProvider) {
            StaticLocationProvider staticProvider = (StaticLocationProvider) provider;
            config.set("config.location.x", staticProvider.getOriginalLocation().getX());
            config.set("config.location.y", staticProvider.getOriginalLocation().getY());
            config.set("config.location.z", staticProvider.getOriginalLocation().getZ());
        }
    }   
    
    public LocationProvider getProvider() {
        return provider;
    }
}
