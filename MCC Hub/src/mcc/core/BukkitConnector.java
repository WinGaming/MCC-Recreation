package mcc.core;

import mcc.core.players.EventPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BukkitConnector {

    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, CoreTest.TEMP_INSTANCE);
    }

    public EventPlayer getEventPlayer(Player player) {
        return this.getEventPlayer(player.getUniqueId());
    }

    public EventPlayer getEventPlayer(UUID uuid) {
        return MCCEvent.getInstance().getTeamManager().getTeams().get(0).getPlayers().get(0);
    }
}
