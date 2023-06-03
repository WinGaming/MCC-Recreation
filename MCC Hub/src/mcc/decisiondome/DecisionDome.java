package mcc.decisiondome;

import mcc.decisiondome.runner.DecisionDomeStateRunner;
import mcc.decisiondome.selector.FieldSelector;
import mcc.event.Event;
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

	private DecisionDomeManipulator manipulator;

	private boolean forceStateUpdate = false;
	
	protected DecisionDome(Event event, DecisionField[] fields, HubDecisiondomeConfig config, TeamBox[] teamBoxes, FieldSelector selector, GameTask gametask) {
		this.event = event;
		this.config = config;
		this.fields = fields;
		this.gameTask = gametask;
		this.teamBoxes = teamBoxes;
		this.fieldSelector = selector;
		this.state = DecisionDomeState.WAITING;
		this.stateRunner = this.state.createStateRunner(this, this.manipulator); // TODO: this is possibly not defined yet
		// TODO: this.manipulator is not defined there

		this.manipulator = new DecisionDomeManipulator() {
			@Override
			public DecisionField[] getActiveDecisionFields() {
				return DecisionDome.this.fields;
			}

			@Override
			public int getChoosenPosition() {
				return DecisionDome.this.chosenPosition;
			}

			@Override
			public int getCurrentSelection() {
				return DecisionDome.this.currentSelectionIndex;
			}

			@Override
			public FieldSelector getFieldSelector() {
				return DecisionDome.this.fieldSelector;
			}

			@Override
			public GameTask getGameTask() {
				return DecisionDome.this.gameTask;
			}

			@Override
			public void forceStateUpdate() {
				DecisionDome.this.forceStateUpdate = true;
			}

			@Override
			public void setChosenPosition(int index) {
				DecisionDome.this.chosenPosition = index;
			}

			@Override
			public void switchToGame() {
				DecisionDome.this.event.switchToGame();
			}
		};
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
	
	public void tick() {
		this.currentSelectionIndex = this.stateRunner.updateSelectedField();

		boolean needFieldsUpdate = this.stateRunner.tick();

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
}
