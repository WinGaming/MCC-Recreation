package mcc.core;

import mcc.core.players.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class BukkitConnector {

    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, CoreTest.TEMP_INSTANCE);
    }

    public EventPlayer getEventPlayer(Player player) {
        return new DummyEventPlayer();
    }
}
