package mcc.locationprovider;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * This functional interface allows to provide an infinite number of locations.
 */
@FunctionalInterface
public interface LocationProvider {
    
    Location next(World world);

}
