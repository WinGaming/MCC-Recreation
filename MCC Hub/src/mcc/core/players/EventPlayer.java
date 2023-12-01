package mcc.core.players;

import org.bukkit.Bukkit;

import java.util.UUID;

public class EventPlayer {

    private final String displayName;
    private final UUID uniqueId;
    private final PlayerStatistics stats;

    private boolean isReady = false;

    public EventPlayer(String displayName, UUID uniqueId, PlayerStatistics stats) {
        this.displayName = displayName;
        this.uniqueId = uniqueId;
        this.stats = stats;
    }


    public boolean isReady() {
        return this.isReady;
    }

    public void resetReady() {
        this.isReady = false;
    }

    public void markReady() {
        this.isReady = true;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean isOnline() {
        return true || Bukkit.getPlayer(this.getUniqueId()) != null; // TODO:
    }

    public PlayerStatistics getStatistics() {
        return this.stats;
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }
}
