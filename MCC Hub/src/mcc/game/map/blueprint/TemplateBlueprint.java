package mcc.game.map.blueprint;

import org.bukkit.Location;

import mcc.utils.Vector3d;
import mcc.utils.Vector3i;

public class TemplateBlueprint implements MapBlueprint {

    private Location templateStart;
    private Vector3i size;

    private Vector3d relativeSpawn;

    public TemplateBlueprint(Location templateStart, Vector3i size, Vector3d relativeSpawn) {
        this.templateStart = templateStart;
        this.size = size;
        this.relativeSpawn = relativeSpawn;
    }

    @Override
    public Vector3i getSize() {
        return this.size;
    }

    @Override
    public void build(Location startLocation) { // TODO: Do this better and with entitites
        for (int x = 0; x < this.size.getX(); x++) {
            for (int y = 0; y < this.size.getY(); y++) {
                for (int z = 0; z < this.size.getZ(); z++) {
                    startLocation.clone().add(x, y, z).getBlock().setType(this.templateStart.clone().add(x, y, z).getBlock().getType());
                }
            }
        }
    }

    @Override
    public Vector3d getRelativeLobbySpawn() {
        return this.relativeSpawn;
    }
}
