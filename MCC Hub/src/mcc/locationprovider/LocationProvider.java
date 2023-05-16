package mcc.locationprovider;

import mcc.utils.Vector3d;

/**
 * This functional interface allows to provide an infinite number of locations.
 */
@FunctionalInterface
public interface LocationProvider {
    
    Vector3d next();

}
