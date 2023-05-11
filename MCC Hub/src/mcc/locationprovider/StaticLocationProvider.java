package mcc.locationprovider;

import org.bukkit.Location;

public class StaticLocationProvider implements LocationProvider {

    private Location location;

    public StaticLocationProvider(Location location) {
        this.location = location;
    }

    @Override
    public Location next() {
        return this.location.clone();
    }

    public Location getOriginalLocation() {
        return location;
    }
}
