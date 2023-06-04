package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.Timer;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.BOLD;;

public class GameSelectionIntroDecisionDomeStateRunner extends DecisionDomeStateRunner {

    public GameSelectionIntroDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);
    }

    @Override
    public Timer setup() {
        DecisionField[] fields = this.getDecisionDome().getActiveDecisionFields();
        for (int i = 0; i < fields.length; i++) fields[i].setState(DecisionFieldState.DISABLED);

        return Timer.fromConfig(this.getDecisionDome().getConfig().getGameSelectionPreVoteTimer());
    }

    @Override
    public int updateSelectedField() {
        return -1;
    }

    @Override
    public boolean tick() {
        return false;
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        return DecisionDomeState.GAME_SELECTION;
    }

    @Override
    public String getTimerTitle() {
        return RED + "" + BOLD + "Voting begins in:";
    }
}
