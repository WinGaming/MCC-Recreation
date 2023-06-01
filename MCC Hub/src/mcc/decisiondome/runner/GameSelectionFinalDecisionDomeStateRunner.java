package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.Timer;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.decisiondome.TimerConfig;

public class GameSelectionFinalDecisionDomeStateRunner extends DecisionDomeStateRunner {

    private int ticksWaited;

    public GameSelectionFinalDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);

        this.ticksWaited = 0;
    }

    @Override
    public Timer setup() {
        this.ticksWaited = 0;
        return Timer.fromConfig(this.getDecisionDome().getConfig().getGameSelectionFinalTimer());
    }

    @Override
    public int updateSelectedField(int current) {
        double totalRemaining = (double) this.getDecisionDome().getCurrentTimer().remaining(System.currentTimeMillis());
        
        HubDecisiondomeConfig config = this.getDecisionDome().getConfig();

        TimerConfig selectionFinalTimes = config.getGameSelectionFinalTimer();
        double selectionFinalTimer = selectionFinalTimes.getTimeunit().toMillis(selectionFinalTimes.getAmount());

        TimerConfig selectionTimes = config.getGameSelectionTimer();
        double selectionTimer = selectionTimes.getTimeunit().toMillis(selectionTimes.getAmount());

        double percentage = totalRemaining / (selectionFinalTimer + selectionTimer);
        double minDelay = config.getMinTickDelay();
        double maxAddedDelay = config.getMaxAdditionalTickDelay();
        int ticksToWait = (int) Math.ceil(minDelay + maxAddedDelay * percentage);
        if (this.ticksWaited >= ticksToWait) {
            current++;
            current %= this.getDecisionDome().getFieldCount();
            this.ticksWaited = 0;
        } else {
            this.ticksWaited++;
        }

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
