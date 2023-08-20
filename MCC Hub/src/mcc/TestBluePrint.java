package mcc;

import org.bukkit.Location;

import mcc.game.map.blueprint.MapBlueprint;
import mcc.utils.Vector3d;
import mcc.utils.Vector3i;

public class TestBluePrint implements MapBlueprint {
    
    @Override
    public Vector3i getSize() {
        return new Vector3i(10, 10, 10);
    }

    @Override
    public void build(Location startLocation) {
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                startLocation.clone().add(x, 0, z).getBlock().setType(org.bukkit.Material.WHITE_WOOL);
            }
        }
    }

    @Override
    public Vector3d getRelativeLobbySpawn() {
        return new Vector3d(0, 10, 0);
    }
}
