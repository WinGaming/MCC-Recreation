package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.EmptyTimer;
import mcc.timer.Timer;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.BOLD;

public class GameSelectionAwaitChosenPositionHighlightDecisionDomeStateRunner extends DecisionDomeStateRunner {

    private int ticksWaited;

    public GameSelectionAwaitChosenPositionHighlightDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);

        this.ticksWaited = 0;
    }

    @Override
    public Timer setup() {
        this.ticksWaited = 0;
        return new EmptyTimer();
    }

    @Override
    public int updateSelectedField() {
        int current = this.getDecisionDome().getCurrentSelection();
        int chosenPosition = this.getDecisionDome().getChoosenPosition();

        double delay = this.getDecisionDome().getConfig().getMinTickDelay();
        int ticksToWait = (int) delay;
        if (this.ticksWaited >= ticksToWait) {
            current++;
            current %= this.getDecisionDome().getFieldCount();
            this.ticksWaited = 0;

            if (current == chosenPosition) {
                this.getDecisionDome().forceStateUpdate();
            }
        } else {
            this.ticksWaited++;
        }

        return current;
    }

    @Override
    public boolean tick(long now) {
        DecisionField[] fields = this.getDecisionDome().getActiveDecisionFields();
        for (int i = 0; i < fields.length; i++) fields[i].setState(i == this.getDecisionDome().getCurrentSelection() ? DecisionFieldState.HIGHLIGHTED : DecisionFieldState.ENABLED);
        return true;
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        return DecisionDomeState.GAME_SELECTED_AWAIT_TELEPORT;
    }

    @Override
    public String getTimerTitle() {
        return RED + "" + BOLD + "Game chosen in:";
    }
}
