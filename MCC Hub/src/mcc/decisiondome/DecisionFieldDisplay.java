package mcc.decisiondome;

public interface DecisionFieldDisplay {
    
    /** Resets the display to displaying no game. */
    void reset();

    /** Starts a rolling animation or similar, waiting for {@code displayGame} to be called. */
    void startRolling();

    /** Displays the game given by the {@code gameKey}. */
    void displayGame(String gameKey);
}
