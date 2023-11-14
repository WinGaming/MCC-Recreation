package mcc.core.players;

public interface EventPlayer {

    String getDisplayName();

    boolean isOnline();

    PlayerStatistics getStatistics();

}
