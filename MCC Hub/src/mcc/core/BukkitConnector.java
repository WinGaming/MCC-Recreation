package mcc.core;

import mcc.core.players.EventPlayer;
import org.bukkit.entity.Player;

public class BukkitConnector {

    public EventPlayer getEventPlayer(Player player) {
        return new DummyEventPlayer();
    }
}
