package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeManipulator;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.Timer;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.decisiondome.TimerConfig;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.BOLD;

public class GameSelectionDecisionDomeStateRunner extends DecisionDomeStateRunner {

    private int ticksWaited;

    public GameSelectionDecisionDomeStateRunner(DecisionDome decisionDome, DecisionDomeManipulator manipulator) {
        super(decisionDome, manipulator);

        this.ticksWaited = 0;
    }

    @Override
    public Timer setup() {
        this.ticksWaited = 0;
        return Timer.fromConfig(this.getDecisionDome().getConfig().getGameSelectionTimer());
    }

    @Override
    public int updateSelectedField() {
        int current = this.getManipulator().getCurrentSelection();
        double totalRemaining = (double) this.getDecisionDome().getCurrentTimer().remaining(System.currentTimeMillis());

        HubDecisiondomeConfig config = this.getDecisionDome().getConfig();

        TimerConfig selectionFinalTimes = config.getGameSelectionFinalTimer();
        double selectionFinalTimer = selectionFinalTimes.getTimeunit().toMillis(selectionFinalTimes.getAmount());

        TimerConfig selectionTimes = config.getGameSelectionTimer();
        double selectionTimer = selectionTimes.getTimeunit().toMillis(selectionTimes.getAmount());

        totalRemaining += selectionFinalTimer;

        double percentage = totalRemaining / (selectionFinalTimer + selectionTimer);
        double minDelay = config.getMinTickDelay();
        double maxAddedDelay = config.getMaxAdditionalTickDelay();
        int ticksToWait = (int) Math.ceil(minDelay + maxAddedDelay * percentage);
        if (this.ticksWaited >= ticksToWait) {
            current++;
            current %= getDecisionDome().getFieldCount();
            this.ticksWaited = 0;
        } else {
            this.ticksWaited++;
        }

        return current;
    }

    @Override
    public boolean tick() {
        DecisionField[] fields = this.getManipulator().getActiveDecisionFields();
        for (int i = 0; i < fields.length; i++) fields[i].setState(i == this.getManipulator().getCurrentSelection() ? DecisionFieldState.HIGHLIGHTED : DecisionFieldState.ENABLED);
        return true;
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        return DecisionDomeState.GAME_SELECTION_FINAL;
    }

    @Override
    public String getTimerTitle() {
        return RED + "" + BOLD + "Voting closes in:";
    }
}
