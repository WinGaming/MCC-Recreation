package mcc.yml;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import mcc.MCC;
import mcc.utils.Vector2f;
import mcc.utils.Vector3d;

public class ConfigInstanceUtils {
    
    public static Location instantiateHubSpawnLocation() {
        World world = Bukkit.getWorld(MCC.lobbyConfig.getConfigInstance().getWorldName());
        if (world == null) {
			throw new IllegalArgumentException("Failed to reload: Could not find world \"" + MCC.lobbyConfig.getConfigInstance().getWorldName() + "\"");
		}
        Optional<Vector3d> spawnVector = MCC.lobbyConfig.getConfigInstance().getSpawnLocation();
        if (spawnVector.isEmpty()) {
            throw new IllegalArgumentException("Failed to reload: Spawn location not set");
        }

        Optional<Vector2f> spawnDirection = MCC.lobbyConfig.getConfigInstance().getSpawnRotation();
        if (spawnDirection.isEmpty()) {
            throw new IllegalArgumentException("Failed to reload: Spawn rotation not set");
        }

        return new Location(world, spawnVector.get().getX(), spawnVector.get().getY(), spawnVector.get().getZ(), spawnDirection.get().getX(), spawnDirection.get().getY());
    }
}
