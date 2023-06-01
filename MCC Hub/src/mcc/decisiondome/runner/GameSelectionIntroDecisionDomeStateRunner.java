package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.Timer;

public class GameSelectionIntroDecisionDomeStateRunner extends DecisionDomeStateRunner {

    public GameSelectionIntroDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);
    }

    @Override
    public Timer setup() {
        return Timer.fromConfig(this.getDecisionDome().getConfig().getGameSelectionPreVoteTimer());
    }

    @Override
    public int updateSelectedField(int current) {
        return -1;
    }

    @Override
    public boolean tick() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tick'");
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onTimerFinished'");
    }

    @Override
    public String getTimerTitle() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimerTitle'");
    }
    
}
