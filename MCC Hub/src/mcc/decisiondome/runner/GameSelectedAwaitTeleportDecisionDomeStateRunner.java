package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.Timer;

import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.BOLD;

public class GameSelectedAwaitTeleportDecisionDomeStateRunner extends DecisionDomeStateRunner {

    public GameSelectedAwaitTeleportDecisionDomeStateRunner(DecisionDome decisionDome) {
        super(decisionDome);
    }

    @Override
    public Timer setup() {
        return Timer.fromConfig(this.getDecisionDome().getConfig().getGameSelectedAwaitTeleportTimer());
    }

    @Override
    public int updateSelectedField() {
        this.getDecisionDome().getGameTask().teleportPlayers();
        this.getDecisionDome().forceStateUpdate();
        this.getDecisionDome().switchToGame();

        return this.getDecisionDome().getCurrentSelection();
    }

    @Override
    public boolean tick() {
        DecisionField[] fields = this.getDecisionDome().getActiveDecisionFields();
        // TODO As the fields should not change, this could be in setup right?
        for (int i = 0; i < fields.length; i++) fields[i].setState(i == this.getDecisionDome().getCurrentSelection() ? DecisionFieldState.SELECTED : DecisionFieldState.ENABLED);
        return true;
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        return DecisionDomeState.WAITING;
    }

    @Override
    public String getTimerTitle() {
        return RED + "" + BOLD + "Teleporting to Game in:";
    }
}
