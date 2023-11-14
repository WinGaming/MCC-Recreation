package mcc.core;

import mcc.utils.PlayerTagCache;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CoreTest extends JavaPlugin implements Listener {

    public static CoreTest TEMP_INSTANCE;

    private MCCEvent event;
    @Override
    public void onEnable() {
        TEMP_INSTANCE = this;

        System.out.println("Started CoreTest...");

        this.event = new MCCEvent(this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.event.tick(System.currentTimeMillis()), 1, 0);

        Bukkit.getPluginManager().registerEvents(this, this);

        // Action on command:
        // () -> this.event.setCurrentChapter(...);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerTagCache.clearTags(event.getPlayer().getUniqueId());
    }

    @Override
    public void onDisable() {
        event.destroy();
    }
}
