package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.Timer;

public abstract class DecisionDomeStateRunner {

    private DecisionDome decisionDome;

    public DecisionDomeStateRunner(DecisionDome decisionDome) {
        this.decisionDome = decisionDome;
    }

    public DecisionDome getDecisionDome() {
        return decisionDome;
    }

    /**
     * Called when the state is set.
     * If the returned timer is null, onTimerFinished will never be called.
     * @return a Timer instance when onTimerFinished should be called, or null if it should never be called.
     */
    public abstract Timer setup();

    /**
     * Called every tick while the state is set.
     * The returned index should be set in the same tick.
     * @param current the currently selected field
     * @param chosenPosition the position of the chosen field, or -1 if no field is chosen
     * @return the index of the field to highlight, or -1 if no field should be highlighted.
     */
    public abstract int updateSelectedField(int current, int chosenPosition);

    /**
     * Called every tick, returning if the fields should be updated.
     * @return {@code true} if the fields should be updated, {@code false} otherwise.
     */
    public abstract boolean tick();

    /**
     * Called when the timer returned by setup() runs out.
     * @return the next state to set
     */
    public abstract DecisionDomeState onTimerFinished();

    /**
     * Returns the title of the timer.
     * @return the title of the timer
     */
    public abstract String getTimerTitle();
    
}