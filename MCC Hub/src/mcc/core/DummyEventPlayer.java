package mcc.core;

import mcc.core.players.EventPlayer;
import mcc.core.players.PlayerStatistics;

import java.util.UUID;

public class DummyEventPlayer implements EventPlayer {

    @Override
    public String getDisplayName() {
        return "TestPlayer";
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public PlayerStatistics getStatistics() {
        return new PlayerStatistics();
    }

    private boolean isReady = false;

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public void resetReady() {
        isReady = false;
    }

    @Override
    public void markReady() {
        isReady = true;
    }

    @Override
    public UUID getUniqueId() {
        return UUID.fromString("f039b24f-114e-4eb8-964d-50b7c21090d7");
    }
}
