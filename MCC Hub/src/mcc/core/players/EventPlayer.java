package mcc.core.players;

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


    boolean isReady() {
        return this.isReady;
    }

    void resetReady() {
        this.isReady = false;
    }

    void markReady() {
        this.isReady = true;
    }

    String getDisplayName() {
        return this.displayName;
    }

    boolean isOnline() {
        return true; // TODO:
    }

    PlayerStatistics getStatistics() {
        return this.stats;
    }

    UUID getUniqueId() {
        return this.uniqueId;
    }
}
