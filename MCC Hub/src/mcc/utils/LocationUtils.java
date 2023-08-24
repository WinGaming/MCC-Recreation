package mcc.utils;

import org.bukkit.Location;

public class LocationUtils {
    
    public static Vector3i toVector3i(Location location) {
        return new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
