package mcc.decisiondome.display;

import mcc.decisiondome.DecisionField;

/** 
 * Represents a element that can display a game selection,
 * or if a game is currently being selected
 */
public abstract class DecisionFieldDisplay {
    
    private DecisionField field;

    public DecisionFieldDisplay(DecisionField field) {
        this.field = field;
    }

    public DecisionField getField() {
        return field;
    }

    /** Resets the display to displaying no game. */
    public abstract void reset();

    /** 
     * Starts a rolling animation or similar, waiting for {@code displayGame} to be called.
     * This method may be called multiple times, but only the first call will have an effect.
     */
    public abstract void startRolling();

    /**
     * Displays the game given by the {@code gameKey}.
     * @param gameKey the key for the game to display
     */
    public abstract void displayGame(String gameKey);
}
