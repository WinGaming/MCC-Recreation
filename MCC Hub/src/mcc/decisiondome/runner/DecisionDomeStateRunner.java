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
     * @return the index of the field to highlight, or -1 if no field should be highlighted.
     */
    public abstract int updateSelectedField();

    /**
     * Called every tick, returning if the fields should be updated.
     * @param now the current time in milliseconds
     * @return {@code true} if the fields should be updated, {@code false} otherwise.
     */
    public abstract boolean tick(long now);

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