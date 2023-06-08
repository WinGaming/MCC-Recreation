package mcc.decisiondome.display;

import mcc.decisiondome.DecisionField;

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

    /** Displays the game given by the {@code gameKey}. */
    public abstract void displayGame(String gameKey);
}
