package mcc.core;

import mcc.core.players.EventPlayer;

public class DummyEventPlayer implements EventPlayer {

    @Override
    public String getDisplayName() {
        return "TestPlayer";
    }

    @Override
    public boolean isOnline() {
        return false;
    }
}
