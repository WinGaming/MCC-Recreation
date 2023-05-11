package mcc.locationprovider;

import org.bukkit.Location;

/**
 * This functional interface allows to provide an infinite number of locations.
 */
@FunctionalInterface
public interface LocationProvider {
    
    Location next();

    public static LocationProvider createStaticProvider(Location location) {
        return () -> location.clone();
    }
}
