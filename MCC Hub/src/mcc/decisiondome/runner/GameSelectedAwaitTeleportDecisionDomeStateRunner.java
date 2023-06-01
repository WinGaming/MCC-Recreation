package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.Timer;

public class GameSelectedAwaitTeleportDecisionDomeStateRunner extends DecisionDomeStateRunner {

    public GameSelectedAwaitTeleportDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);
    }

    @Override
    public Timer setup() {
        return Timer.fromConfig(this.getDecisionDome().getConfig().getGameSelectedAwaitTeleportTimer());
    }

    @Override
    public int updateSelectedField(int current) {
        return current;
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
