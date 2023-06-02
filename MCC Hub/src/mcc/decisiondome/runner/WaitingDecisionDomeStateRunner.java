package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.Timer;

public class WaitingDecisionDomeStateRunner extends DecisionDomeStateRunner {

    public WaitingDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);
    }

    @Override
    public Timer setup() {
        for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(DecisionFieldState.DISABLED);

        return null;
    }

    @Override
    public int updateSelectedField(int current, int chosenPosition) {
        return -1;
    }

    @Override
    public boolean tick() {
        return false;
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        System.err.println("Decision dome timer finished, but no timer was set up");
        return DecisionDomeState.WAITING;
    }

    @Override
    public String getTimerTitle() {
        return "null";
    }
}
