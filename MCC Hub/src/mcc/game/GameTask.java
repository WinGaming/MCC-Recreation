package mcc.game;

import mcc.event.Event;

public class GameTask {
    
    private Game currentGame;

    public boolean prepareGame(String gameKey, Event evnet) {
        if (currentGame != null) {
            System.err.println("Tried to start new game while one is still running");
            return false;
        }

        this.currentGame = GameManager.createGame(gameKey, evnet);
        if (this.currentGame == null) {
            System.err.println("Failed to create game: " + gameKey);
            return false;
        }

        this.currentGame.prepare();
        return true;
    }

    /**
     * This teleports all players to the given positions and starts the game itself.
     */
    public void teleportPlayers() {
        if (this.currentGame != null) this.currentGame.teleportPlayers();
    }

    public void tick(long now) {
        if (this.currentGame != null) this.currentGame.tick(now);
    }

}
