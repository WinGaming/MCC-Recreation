package mcc.decisiondome.runner;

import mcc.decisiondome.DecisionDome;
import mcc.decisiondome.DecisionDomeState;
import mcc.decisiondome.DecisionField;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.timer.Timer;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.decisiondome.TimerConfig;

import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;

import static org.bukkit.ChatColor.BOLD;

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
    public int updateSelectedField() {
        int current = this.getDecisionDome().getCurrentSelection();
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
        DecisionField[] fields = this.getDecisionDome().getActiveDecisionFields();
        for (int i = 0; i < fields.length; i++) fields[i].setState(i == this.getDecisionDome().getCurrentSelection() ? DecisionFieldState.HIGHLIGHTED : DecisionFieldState.ENABLED);
        return true;
    }

    @Override
    public DecisionDomeState onTimerFinished() {
        DecisionField[] fields = this.getDecisionDome().getActiveDecisionFields();
        int gameSelection = this.getDecisionDome().getFieldSelector().select(fields);
        if (gameSelection < 0) {
            Bukkit.broadcastMessage("Failed to select a game. The event is now paused until the error gets resolved");
            return null;
        }

        this.getDecisionDome().setChosenPosition(gameSelection);
        int chosenPosition = this.getDecisionDome().getChoosenPosition();

        if (fields.length <= chosenPosition || chosenPosition < 0) {
            Bukkit.broadcastMessage("Tried to setup game from invalid index: " + chosenPosition);
            return null;
        }

        if (!this.getDecisionDome().getGameTask().prepareGame(fields[chosenPosition].getGameKey())) {
            Bukkit.broadcastMessage("Failed to prepare game. The event is now paused until the error gets resolved");
            return null;
        }

        return DecisionDomeState.GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT;
    }

    @Override
    public String getTimerTitle() {
        return RED + "" + BOLD + "Game chosen in:";
    }
}
