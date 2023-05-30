package mcc.locationprovider;

import mcc.utils.Vector3d;

/**
 * A {@code LocationProvider} implementation that always returns a copy of the same location.
 */
public class StaticLocationProvider implements LocationProvider {

    private Vector3d location;

    public StaticLocationProvider(Vector3d location) {
        this.location = location;
    }

    @Override
    public Vector3d next() {
        return this.location.clone();
    }

    public Vector3d getOriginalLocation() {
        return location;
    }
}
