package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.Timer;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.BOLD;;

public class GameSelectionIntroDecisionDomeStateRunner extends DecisionDomeStateRunner {

    // TODO: Config
    private long titleDuration = 2000L;
    private long rollDuration = 2000L;

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
    public boolean tick(long now) {
        long remaining = this.getDecisionDome().getCurrentTimer().remaining(now);

        // The timestamp when the first roll should start
        long startRoll = now + remaining - (this.getDecisionDome().getFieldCount() * (this.rollDuration + this.titleDuration));

        for (int i = 0; i < this.getDecisionDome().getFieldCount(); i++) {
            // The timestamp when the current field should be highlighted
            long start = startRoll + (i * (this.rollDuration + this.titleDuration));

            if (now >= start + this.rollDuration) {
                if (this.getDecisionDome().getFields()[i].getGameKey() == null) {
                    this.getDecisionDome().setGameForField(i);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("", this.getDecisionDome().getFields()[i].getGameKey(), 0, 35, 5);
                    }
                    Bukkit.broadcastMessage("[Decision Dome]: " + this.getDecisionDome().getFields()[i].getGameKey() + "!");
                }
            } else if (now >= start) {
                this.getDecisionDome().getActiveDecisionFields()[i].getDisplay().startRolling();
            }
        }

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
