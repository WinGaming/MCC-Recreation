package mcc.config;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import mcc.indicator.ParticleIndicator;
import mcc.utils.Pair;
import mcc.utils.Vector3i;

public class AreaSelector implements ConfigSelector<Pair<Location, Location>> {
    
    private boolean selectA = true;
    private Location cornerA, cornerB;

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if (selectA) this.cornerA = event.getBlock().getLocation();
        else this.cornerB = event.getBlock().getLocation();

        String sizeString = null;
        if (cornerA != null && cornerB != null) {
            int x = Math.abs(cornerA.getBlockX() - cornerB.getBlockX()) + 1;
            int y = Math.abs(cornerA.getBlockY() - cornerB.getBlockY()) + 1;
            int z = Math.abs(cornerA.getBlockZ() - cornerB.getBlockZ()) + 1;
            sizeString = String.format(" %d x %d x %d (%d)", x, y, z, x * y * z);
        }

        event.getPlayer().sendMessage(String.format("Corner %s selected: %d, %d, %d%s", selectA ? "1" : "2", cornerA.getBlockX(), cornerA.getBlockY(), cornerA.getBlockZ(), sizeString == null ? "" : sizeString));

        selectA = !selectA;
    }

    @Override
    public void displayTick(Player player) {
        if (cornerA == null && cornerB != null) ; // TODO:
        if (cornerA != null && cornerB == null) ; // TODO:
        if (cornerA != null && cornerB != null) {
            World world = cornerA.getWorld();
            Vector3i a = new Vector3i(cornerA.getBlockX(), cornerA.getBlockY(), cornerA.getBlockZ());
            Vector3i b = new Vector3i(cornerB.getBlockX(), cornerB.getBlockY(), cornerB.getBlockZ());
            ParticleIndicator.highlightArea(player, world, a, b);
        }
    }

    @Override
    public Pair<Location, Location> build() {
        return new Pair<>(cornerA, cornerB);
    }
}
