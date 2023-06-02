package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
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
    public int updateSelectedField(int current, int chosenPosition) {
        this.gameTask.teleportPlayers();
        this.setState(DecisionDomeState.WAITING);
        this.event.switchToGame();
        return;

        return current;
    }

    @Override
    public boolean tick() {
        // TODO As the fields should not change, this could be in setup right?
        for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.SELECTED : DecisionFieldState.ENABLED);

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tick'");
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
