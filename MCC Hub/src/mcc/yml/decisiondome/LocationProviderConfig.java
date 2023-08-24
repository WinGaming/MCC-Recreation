package mcc.yml.decisiondome;

import org.bukkit.configuration.ConfigurationSection;

import mcc.locationprovider.LocationProvider;
import mcc.locationprovider.StaticLocationProvider;
import mcc.utils.Vector2f;
import mcc.utils.Vector3d;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration section for a location provider
 * implementing {@link MCCConfigSerializable}.
 */
public class LocationProviderConfig implements MCCConfigSerializable {

    private LocationProvider provider;

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        String type = config.getString("type");

        if (type.equalsIgnoreCase("static")) {
            double x = config.getDouble("config.location.x");
            double y = config.getDouble("config.location.y");
            double z = config.getDouble("config.location.z");

            float yaw = (float) config.getDouble("config.location.yaw");
            float pitch = (float) config.getDouble("config.location.pitch");

            this.provider = new StaticLocationProvider(new Vector3d(x, y, z), new Vector2f(yaw, pitch));
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
            config.set("config.location.yaw", staticProvider.getOriginalDirection().getX());
            config.set("config.location.pitch", staticProvider.getOriginalDirection().getY());
            config.set("type", "static");
        } else {
            config.set("type", "unknown");
        }
    }

    public void setProvider(LocationProvider provider) {
        this.provider = provider;
    }
    
    public LocationProvider getProvider() {
        return provider;
    }
}
