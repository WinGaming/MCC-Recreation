package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.Timer;

public class WaitingDecisionDomeStateRunner extends DecisionDomeStateRunner {

    public WaitingDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);
    }

    @Override
    public Timer setup() {
        DecisionField[] fields = this.getDecisionDome().getActiveDecisionFields();
        for (int i = 0; i < fields.length; i++) fields[i].setState(DecisionFieldState.DISABLED);

        return null;
    }

    @Override
    public int updateSelectedField() {
        return -1;
    }

    @Override
    public boolean tick(long now) {
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
