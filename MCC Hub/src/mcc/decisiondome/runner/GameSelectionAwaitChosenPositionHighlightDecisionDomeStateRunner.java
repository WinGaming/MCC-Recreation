package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.timer.EmptyTimer;
import mcc.timer.Timer;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.BOLD;

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
        for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.HIGHLIGHTED : DecisionFieldState.ENABLED);
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        System.err.println("Decision dome timer finished, but no timer was set up");
        return DecisionDomeState.WAITING;
    }

    @Override
    public String getTimerTitle() {
        return RED + "" + BOLD + "Game chosen in:";
    }
}
