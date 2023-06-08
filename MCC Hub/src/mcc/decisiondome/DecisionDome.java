package mcc.decisiondome;

import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.decisiondome.runner.DecisionDomeStateRunner;
import mcc.decisiondome.selector.FieldSelector;
import mcc.event.Event;
import mcc.game.GameManager;
import mcc.game.GameTask;
import mcc.timer.Timer;
import mcc.yml.decisiondome.HubDecisiondomeConfig;

public class DecisionDome {
	
	private final DecisionField[] fields;
	private final HubDecisiondomeConfig config;
	private final FieldSelector fieldSelector;
	private final TeamBox[] teamBoxes;
	private final GameTask gameTask;
	private final Event event;

	private Timer currentTimer;
	private DecisionDomeState state;
	private DecisionDomeStateRunner stateRunner;
	
	private int currentSelectionIndex;
	
	private int chosenPosition = -1;

	private boolean forceStateUpdate = false;
	
	protected DecisionDome(Event event, DecisionField[] fields, HubDecisiondomeConfig config, TeamBox[] teamBoxes, FieldSelector selector, GameTask gametask) {
		this.event = event;
		this.config = config;
		this.fields = fields;
		this.gameTask = gametask;
		this.teamBoxes = teamBoxes;
		this.fieldSelector = selector;

		this.setState(DecisionDomeState.WAITING);
	}

	public void stop() {
		// TODO: Reset everything
	}
	
	private void setState(DecisionDomeState newState) {
		if (this.state == newState) return;

		System.out.println("Decision dome state changed from " + this.state + " to " + newState);

		this.state = newState;
		this.stateRunner = newState.createStateRunner(this);
			
		this.currentTimer = this.stateRunner.setup();

		if (this.currentTimer != null) this.currentTimer.start(System.currentTimeMillis());
	}

	public void setGameForField(int index) {
		if (this.fields[index].getGameKey() == null) {
			this.fields[index].setGameKey(GameManager.getGameList().get(0)); // TODO:
		}
	}

	public DecisionField[] getFields() {
		return fields;
	}
	
	public void start() {
		// Teleporting all players...
		for (TeamBox box : this.teamBoxes) {
			box.teleportPlayers();
		}

		this.chosenPosition = -1;

		if (this.state == DecisionDomeState.WAITING) {
			this.setState(DecisionDomeState.GAME_SELECTION_INTRO);
		} else {
			System.err.println("Can not start decision dome, it's already running!");
		}
	}
	
	public void tick(long now) {
		this.currentSelectionIndex = this.stateRunner.updateSelectedField();

		boolean needFieldsUpdate = this.stateRunner.tick(now);

		if (this.forceStateUpdate || this.currentTimer != null && this.currentTimer.remaining(System.currentTimeMillis()) <= 0) {
			this.forceStateUpdate = false;

			DecisionDomeState newState = this.stateRunner.onTimerFinished();
			if (newState == null) newState = DecisionDomeState.WAITING;
			setState(newState);
		}

		if (needFieldsUpdate) { // TODO: Run on state change
			for (DecisionField field : this.fields) {
				field.tick();
			}
		}
	}

	public int getFieldCount() {
		return this.fields.length;
	}

	public HubDecisiondomeConfig getConfig() {
		return config;
	}

	public DecisionDomeState getState() {
		return state;
	}

	public String getTimerTitle() {
		return this.stateRunner.getTimerTitle();
	}

	public Timer getCurrentTimer() {
		return currentTimer;
	}

	/**
     * Returns an array of all fields that are not disabled by the DecisionDome itself.
     * Fields may be disabled by the DecisionDome to limit the possible selectable fields in later rounds.
     * @return an array of all fields that are no disabled by the DecisionDome itself
     */
	public DecisionField[] getActiveDecisionFields() {
		return DecisionDome.this.fields;
	}

	public int getChoosenPosition() {
		return DecisionDome.this.chosenPosition;
	}

	// * @param current the currently selected field
	public int getCurrentSelection() {
		return DecisionDome.this.currentSelectionIndex;
	}

	// * @param chosenPosition the position of the chosen field, or -1 if no field is chosen
	public FieldSelector getFieldSelector() {
		return DecisionDome.this.fieldSelector;
	}

	public GameTask getGameTask() {
		return DecisionDome.this.gameTask;
	}

	public void forceStateUpdate() {
		DecisionDome.this.forceStateUpdate = true;
	}

	public void setChosenPosition(int index) {
		DecisionDome.this.chosenPosition = index;
	}

	public void switchToGame() {
		DecisionDome.this.event.switchToGame();
	}
}
