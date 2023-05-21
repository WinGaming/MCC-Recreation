package mcc.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import mcc.indicator.ParticleIndicator;
import mcc.utils.Pair;
import mcc.utils.Vector3i;

public class AreaSelector implements ConfigSelector<Pair<Location, Location>> {
    
    private Location cornerA, cornerB;

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onBlockBreak'");
    }

    @Override
    public void displayTick(Player player) {
        if (cornerA == null && cornerB != null) ; // TODO:
        if (cornerA != null && cornerB == null) ; // TODO:
        if (cornerA != null && cornerB != null) {
            World world = cornerA.getWorld();
            Vector3i a = new Vector3i(cornerA.getBlockX(), cornerA.getBlockY(), cornerA.getBlockZ());
            Vector3i b = new Vector3i(cornerB.getBlockX(), cornerB.getBlockY(), cornerB.getBlockZ());
            ParticleIndicator.highlightArea(world, a, b);
        }
    }

    @Override
    public Pair<Location, Location> build() {
        return new Pair<>(cornerA, cornerB);
    }
}
