package mcc.game.map.blueprint;

import org.bukkit.Location;

import mcc.utils.Vector3d;
import mcc.utils.Vector3i;

public interface MapBlueprint {
    
    Vector3i getSize();

    void build(Location startLocation);

    Vector3d getRelativeLobbySpawn();

}
