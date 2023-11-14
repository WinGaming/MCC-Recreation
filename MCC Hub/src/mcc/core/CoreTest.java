package mcc.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreTest extends JavaPlugin {

    public static CoreTest TEMP_INSTANCE;

    private MCCEvent event;
    @Override
    public void onEnable() {
        TEMP_INSTANCE = this;

        System.out.println("Started CoreTest");

        this.event = new MCCEvent(this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.event.tick(System.currentTimeMillis()), 1, 0);

        // Action on command:
        // () -> this.event.setCurrentChapter(...);
    }

    @Override
    public void onDisable() {
        event.destroy();
    }
}
