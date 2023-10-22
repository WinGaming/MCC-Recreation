package mcc.core;

import mcc.indicator.ParticleIndicator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class CoreTest extends JavaPlugin {

    private MCCEvent event;
    @Override
    public void onEnable() {
        System.out.println("Started CoreTest");

        this.event = new MCCEvent();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.event.tick(System.currentTimeMillis()), 1, 0);
    }

    @Override
    public void onDisable() {
        event.destroy();
    }
}
