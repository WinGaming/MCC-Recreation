package mcc.decisiondome;

import java.util.function.Function;

import mcc.decisiondome.runner.DecisionDomeStateRunner;
import mcc.decisiondome.runner.GameSelectedAwaitTeleportDecisionDomeStateRunner;
import mcc.decisiondome.runner.GameSelectionAwaitChosenPositionHighlightDecisionDomeStateRunner;
import mcc.decisiondome.runner.GameSelectionDecisionDomeStateRunner;
import mcc.decisiondome.runner.GameSelectionFinalDecisionDomeStateRunner;
import mcc.decisiondome.runner.GameSelectionIntroDecisionDomeStateRunner;
import mcc.decisiondome.runner.WaitingDecisionDomeStateRunner;

public enum DecisionDomeState {
    /** Paused or any other state where no countdown is active */
    WAITING(WaitingDecisionDomeStateRunner::new),
    /** Timer before voting allowing for explanation */
    GAME_SELECTION_INTRO(GameSelectionIntroDecisionDomeStateRunner::new),
    /** Teams can use items to change the result */
    GAME_SELECTION(GameSelectionDecisionDomeStateRunner::new),
    /** Teams can't use any items anymore */
    GAME_SELECTION_FINAL(GameSelectionFinalDecisionDomeStateRunner::new),
    /** Waiting for selected field to reach chosen */
    GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT(true, GameSelectionAwaitChosenPositionHighlightDecisionDomeStateRunner::new),
    /** A game was selected and players should teleport currently or have been teleported */
    GAME_SELECTED_AWAIT_TELEPORT(GameSelectedAwaitTeleportDecisionDomeStateRunner::new);
    
    private boolean updateFieldsWithoutActiveTimer;
    private Function<DecisionDome, DecisionDomeStateRunner> runnerSupplier;

    private DecisionDomeState(Function<DecisionDome, DecisionDomeStateRunner> runnerSupplier) {
        this(false, runnerSupplier);
    }
    
    private DecisionDomeState(boolean updateFieldsWithoutActiveTimer, Function<DecisionDome, DecisionDomeStateRunner> runnerSupplier) {
        this.updateFieldsWithoutActiveTimer = updateFieldsWithoutActiveTimer;
        this.runnerSupplier = runnerSupplier;
    }
    
    public boolean shouldUpdateFieldsWithoutActiveTimer() {
        return updateFieldsWithoutActiveTimer;
    }

    public DecisionDomeStateRunner createStateRunner(DecisionDome decisionDome) {
        return this.runnerSupplier.apply(decisionDome);
    }
}
