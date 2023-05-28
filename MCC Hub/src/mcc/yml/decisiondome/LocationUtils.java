package mcc.yml.decisiondome;

import org.bukkit.Location;

import mcc.utils.Vector3i;

public class LocationUtils {
    
    public static Vector3i toVector3i(Location location) {
        return new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
