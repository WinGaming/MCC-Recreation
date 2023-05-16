package mcc.decisiondome;

public enum DecisionDomeState {
    /** Paused or any other state where no countdown is active */
    WAITING,
    /** Timer before voting allowing for explanation */
    GAME_SELECTION_INTRO,
    /** Teams can use items to change the result */
    GAME_SELECTION,
    /** Teams can't use any items anymore */
    GAME_SELECTION_FINAL,
    /** Waiting for selected field to reach chosen */
    GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT(true),
    /** A game was selected */
    GAME_SELECTED,
    /** A game was selected and players should teleport currently or have been teleported */
    GAME_SELECTED_AWAIT_TELEPORT;
    
    private boolean updateFieldsWithoutActiveTimer;

    private DecisionDomeState() {
        this(false);
    }
    
    private DecisionDomeState(boolean updateFieldsWithoutActiveTimer) {
        this.updateFieldsWithoutActiveTimer = updateFieldsWithoutActiveTimer;
    }
    
    public boolean shouldUpdateFieldsWithoutActiveTimer() {
        return updateFieldsWithoutActiveTimer;
    }
}