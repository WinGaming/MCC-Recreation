package mcc.core;

import mcc.core.players.EventPlayer;
import mcc.core.players.PlayerStatistics;

public class DummyEventPlayer implements EventPlayer {

    @Override
    public String getDisplayName() {
        return "TestPlayer";
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public PlayerStatistics getStatistics() {
        return new PlayerStatistics();
    }
}
