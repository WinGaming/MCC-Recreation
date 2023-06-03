package mcc.decisiondome;

import mcc.decisiondome.selector.FieldSelector;
import mcc.game.GameTask;

public interface DecisionDomeManipulator {

    /**
     * Returns an array of all fields that are not disabled by the DecisionDome itself.
     * Fields may be disabled by the DecisionDome to limit the possible selectable fields in later rounds.
     * @return an array of all fields that are no disabled by the DecisionDome itself
     */
    DecisionField[] getActiveDecisionFields();

    // * @param current the currently selected field
    int getCurrentSelection();
    // * @param chosenPosition the position of the chosen field, or -1 if no field is chosen
    int getChoosenPosition();

    FieldSelector getFieldSelector();

    GameTask getGameTask();

    void setChosenPosition(int index);

    void forceStateUpdate();
    void switchToGame();
}