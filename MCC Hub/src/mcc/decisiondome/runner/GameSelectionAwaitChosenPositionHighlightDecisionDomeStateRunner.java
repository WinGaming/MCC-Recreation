package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.EmptyTimer;
import mcc.timer.Timer;

public class GameSelectionAwaitChosenPositionHighlightDecisionDomeStateRunner extends DecisionDomeStateRunner {

    private int ticksWaited;
    private boolean finished;

    public GameSelectionAwaitChosenPositionHighlightDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);

        this.ticksWaited = 0;
        this.finished = false;
    }

    @Override
    public Timer setup() {
        this.ticksWaited = 0;
        this.finished = false;
        return new EmptyTimer();
    }

    @Override
    public int updateSelectedField(int current, int chosenPosition) {
        double delay = this.getDecisionDome().getConfig().getMinTickDelay();
        int ticksToWait = (int) delay;
        if (this.ticksWaited >= ticksToWait) {
            current++;
            current %= this.getDecisionDome().getFieldCount();
            this.ticksWaited = 0;

            if (current == chosenPosition) {
                this.finished = true;
                this.setState(DecisionDomeState.GAME_SELECTED_AWAIT_TELEPORT);
            }
        } else {
            this.ticksWaited++;
        }
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
