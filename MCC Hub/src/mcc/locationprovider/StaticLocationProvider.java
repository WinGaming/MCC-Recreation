package mcc.locationprovider;

import mcc.utils.Vector3d;

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
