package mcc.locationprovider;

import org.bukkit.Location;
import org.bukkit.World;

import mcc.utils.Vector2f;
import mcc.utils.Vector3d;

/**
 * A {@code LocationProvider} implementation that always returns a copy of the same location.
 */
public class StaticLocationProvider implements LocationProvider {

    private Vector3d location;
    private Vector2f direction;

    public StaticLocationProvider(Vector3d location, Vector2f direction) {
        this.location = location;
        this.direction = direction;
    }

    @Override
    public Location next(World world) {
        return new Location(world, this.location.getX(), this.location.getY(), this.location.getZ(), this.direction.getX(), this.direction.getY());
    }

    public Vector3d getOriginalLocation() {
        return location;
    }

    public Vector2f getOriginalDirection() {
        return direction;
    }
}
