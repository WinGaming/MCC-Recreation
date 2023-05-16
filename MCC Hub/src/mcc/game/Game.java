package mcc.game;

public abstract class Game {

    /** Do things like load world etc, will get called as soon as selection done (but maybe not yet displayed) */
    public abstract void prepare();

    /** Teleport all players, including spectators to the game */
    public abstract void teleportPlayers();

    /** Called each tick to allow synchronized calls to Server- and MCC-API */
    public abstract void tick();
}
