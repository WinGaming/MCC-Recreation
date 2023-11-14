package mcc.core.display;

import mcc.core.BukkitConnector;
import mcc.core.players.EventPlayer;
import mcc.core.players.PlayerStatistics;
import mcc.display.ScoreboardPartProvider;
import mcc.utils.Pair;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class PreEventCoinsPartProvider implements ScoreboardPartProvider {

    private BukkitConnector bukkitConnector;
    private String prevEventId;

    public PreEventCoinsPartProvider(BukkitConnector bukkitConnector, String prevEventId) {
        this.bukkitConnector = bukkitConnector;
        this.prevEventId = prevEventId;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        EventPlayer eventPlayer = this.bukkitConnector.getEventPlayer(viewer);
        PlayerStatistics stats = eventPlayer.getStatistics();

        String eventCoinsString;
        eventCoinsString = GREEN + "" + BOLD + "Last Event Coins: " + RESET + stats.getEventCoins(this.prevEventId) + "\ue016";

        String lifetimeString = GREEN + "" + BOLD + "Lifetime Coins: " + RESET + stats.getTotalCoins() + "\ue016";

        return new Pair<>(new String[] { eventCoinsString, lifetimeString }, System.currentTimeMillis());
    }
}
