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
        var teams = MCCEvent.getInstance().getTeamManager().getTeams();
        for (var team : teams) {
            for (var eventPlayer : team.getPlayers()) {
                if (eventPlayer.getUniqueId().equals(uuid)) {
                    return eventPlayer;
                }
            }
        }

        return null;
    }
}
