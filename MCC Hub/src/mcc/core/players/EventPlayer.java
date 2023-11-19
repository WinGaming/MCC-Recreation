package mcc.core.players;

import java.util.UUID;

public interface EventPlayer {

    String getDisplayName();

    boolean isOnline();

    PlayerStatistics getStatistics();

    boolean isReady();

    void resetReady();

    void markReady();

    UUID getUniqueId();
}
