package mcc.core.components;

import mcc.core.Component;
import mcc.indicator.ParticleIndicator;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

public class TestComponent implements Component {

        @Override
        public void init() {
            Bukkit.getWorld("world").getBlockAt(269, 35, 257).setType(Material.EMERALD_ORE);
        }

        @Override
        public void tick(long now) {
            ParticleIndicator.highlightBlocks(Bukkit.getWorld("world"), List.of(Bukkit.getWorld("world").getBlockAt(269, 35, 257).getLocation()));
        }

        @Override
        public void destroy() {
            Bukkit.getWorld("world").getBlockAt(269, 35, 257).setType(Material.REDSTONE_ORE);
        }
}
