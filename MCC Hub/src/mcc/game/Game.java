package mcc.game;

public abstract class Game {

    /** Do things like load world etc, will get called as soon as selection done (but maybe not yet displayed) */
    public abstract void prepare();

    /** Teleport all players, including spectators to the game */
    public abstract void teleportPlayers();

    /**
     * Called each tick to allow synchronized calls to Server- and MCC-API.
     * @param now is the timestamp this tick is executed at
     * @returns {@code true} iff the game ended.
     */
    public abstract boolean tick(long now);
}
