package mcc.game;

public abstract class Game {

    /** Do things like load world etc, will get called as soon as selection done (but maybe not yet displayed) */
    abstract void prepare();

    /** Teleport all players, including spectators to the game */
    abstract void teleportPlayers();

}
