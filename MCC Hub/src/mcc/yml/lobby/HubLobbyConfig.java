package mcc.yml.lobby;

import java.util.Optional;

import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Vector2f;
import mcc.utils.Vector3d;
import mcc.yml.MCCConfigSerializable;

public class HubLobbyConfig implements MCCConfigSerializable {

    private String worldName = "WORLD_NAME";

    private Optional<Vector3d> spawnLocation = Optional.empty();
    private Optional<Vector2f> spawnRotation = Optional.empty();

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        boolean valuesChanged = false;

		// World
		if (config.contains("world")) { this.worldName = config.getString("world"); }
		else { this.worldName = "WORLD_NAME"; valuesChanged = true; }

        // Spawn Location
        if (config.contains("spawn")) {
            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            this.spawnLocation = Optional.of(new Vector3d(x, y, z));

            float yaw = (float) config.getDouble("spawn.yaw");
            float pitch = (float) config.getDouble("spawn.pitch");
            this.spawnRotation = Optional.of(new Vector2f(yaw, pitch));
        }

        return valuesChanged;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set("world", this.worldName);

        // Spawn Location
        if (this.spawnLocation.isPresent()) {
            config.set("spawn.x", this.spawnLocation.get().getX());
            config.set("spawn.y", this.spawnLocation.get().getY());
            config.set("spawn.z", this.spawnLocation.get().getZ());
        }
    }

    public void setSpawnLocation(Vector3d spawnLocation) {
        this.spawnLocation = Optional.of(spawnLocation);
    }

    public String getWorldName() {
        return worldName;
    }

    public Optional<Vector3d> getSpawnLocation() {
        return spawnLocation;
    }

    public Optional<Vector2f> getSpawnRotation() {
        return spawnRotation;
    }
}
