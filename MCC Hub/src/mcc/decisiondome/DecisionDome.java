package mcc.decisiondome;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

import org.bukkit.Bukkit;

import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.decisiondome.runner.DecisionDomeStateRunner;
import mcc.decisiondome.selector.FieldSelector;
import mcc.event.Event;
import mcc.game.GameTask;
import mcc.timer.EmptyTimer;
import mcc.timer.Timer;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.decisiondome.TimerConfig;

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
	
	private int ticksWaited = 0;
	private int currentSelectionIndex;
	
	private int chosenPosition = -1;
	
	protected DecisionDome(Event event, DecisionField[] fields, HubDecisiondomeConfig config, TeamBox[] teamBoxes, FieldSelector selector, GameTask gametask) {
		this.event = event;
		this.config = config;
		this.fields = fields;
		this.gameTask = gametask;
		this.teamBoxes = teamBoxes;
		this.fieldSelector = selector;
		this.state = DecisionDomeState.WAITING;
		this.stateRunner = this.state.createStateRunner(this); // TODO: this is possibly not defined yet
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
		this.currentSelectionIndex = this.stateRunner.updateSelectedField(this.currentSelectionIndex, this.chosenPosition);

		// TODO: Remove
		if (this.state != DecisionDomeState.WAITING) {
			if (this.state == DecisionDomeState.GAME_SELECTION || this.state == DecisionDomeState.GAME_SELECTION_FINAL) {
				double totalRemaining = (double) this.currentTimer.remaining(System.currentTimeMillis());
				
				TimerConfig selectionFinalTimes = this.config.getGameSelectionFinalTimer();
				double selectionFinalTimer = selectionFinalTimes.getTimeunit().toMillis(selectionFinalTimes.getAmount());
				
				TimerConfig selectionTimes = this.config.getGameSelectionTimer();
				double selectionTimer = selectionTimes.getTimeunit().toMillis(selectionTimes.getAmount());
				
				if (state == DecisionDomeState.GAME_SELECTION) totalRemaining += selectionFinalTimer;
				
				// Bukkit.broadcastMessage("Total remaining: " + totalRemaining + " (" + selectionFinalTimer + " + " + selectionTimer + ")");
				double percentage = totalRemaining / (selectionFinalTimer + selectionTimer);
				double minDelay = this.config.getMinTickDelay();
				double maxAddedDelay = this.config.getMaxAdditionalTickDelay();
				int ticksToWait = (int) Math.ceil(minDelay + maxAddedDelay * percentage);
				if (this.ticksWaited >= ticksToWait) {
					this.currentSelectionIndex++;
					this.currentSelectionIndex %= this.fields.length;
					this.ticksWaited = 0;
				} else {
					this.ticksWaited++;
				}
			} else if (this.state == DecisionDomeState.GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT) {
				double delay = this.config.getMinTickDelay();
				int ticksToWait = (int) delay;
				if (this.ticksWaited >= ticksToWait) {
					this.currentSelectionIndex++;
					this.currentSelectionIndex %= this.fields.length;
					this.ticksWaited = 0;
					
					if (this.currentSelectionIndex == this.chosenPosition) {
						this.setState(DecisionDomeState.GAME_SELECTED_AWAIT_TELEPORT); // we continue the method here, because we want to show the correct highlighting
					}
				} else {
					this.ticksWaited++;
				}
			} else if (this.state == DecisionDomeState.GAME_SELECTED_AWAIT_TELEPORT) {
				this.gameTask.teleportPlayers();
				this.setState(DecisionDomeState.WAITING);
				this.event.switchToGame();
				return;
			}

			this.stateRunner.tick();
			
			if (this.currentTimer != null || this.state.shouldUpdateFieldsWithoutActiveTimer()) {
				if (this.currentTimer != null) System.out.println(this.currentTimer.buildText(System.currentTimeMillis()));
				
				switch (this.state) {
				case WAITING:
				case GAME_SELECTION_INTRO:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(DecisionFieldState.DISABLED);
					break;
				case GAME_SELECTION:
				case GAME_SELECTION_FINAL:
				case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.HIGHLIGHTED : DecisionFieldState.ENABLED);
					break;
				case GAME_SELECTED_AWAIT_TELEPORT:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.SELECTED : DecisionFieldState.ENABLED);
					break;
				}
			}
			if (this.currentTimer != null && this.currentTimer.remaining(System.currentTimeMillis()) <= 0) {
				this.setState(this.stateRunner.onTimerFinished());
				switch (this.state) {
				case WAITING: System.err.println("A timer run out where it shouldn't!"); break;
				case GAME_SELECTION_INTRO: Bukkit.broadcastMessage("Finished GAME_SELECTION_INTRO"); setState(DecisionDomeState.GAME_SELECTION); break;
				case GAME_SELECTION: Bukkit.broadcastMessage("Finished GAME_SELECTION"); setState(DecisionDomeState.GAME_SELECTION_FINAL); break;
				case GAME_SELECTION_FINAL:
					Bukkit.broadcastMessage("Finished GAME_SELECTION_FINAL");

					int gameSelection = this.fieldSelector.select(this.fields);
					if (gameSelection < 0) {
						setState(DecisionDomeState.WAITING);
						Bukkit.broadcastMessage("Failed to select a game. The event is now paused until the error gets resolved");
						return;
					}

					this.chosenPosition = gameSelection;

					if (this.fields.length <= this.chosenPosition || this.chosenPosition < 0) {
						setState(DecisionDomeState.WAITING);
						Bukkit.broadcastMessage("Tried to setup game from invalid index: " + this.chosenPosition);
						return;
					}

					if (!this.gameTask.startGame(this.fields[this.chosenPosition].getGameKey())) {
						setState(DecisionDomeState.WAITING);
						Bukkit.broadcastMessage("Failed to prepare game. The event is now paused until the error gets resolved");
						return;
					}

					setState(DecisionDomeState.GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT);
					break;
				case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT: System.err.println("A timer run out where it shouldn't!"); break;
				case GAME_SELECTED_AWAIT_TELEPORT:
					Bukkit.broadcastMessage("Finished GAME_SELECTED_AWAIT_TELEPORT");

					setState(DecisionDomeState.WAITING);
					break;
				}
			}
			
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
		switch (this.state) {
			case GAME_SELECTION_INTRO:
			return RED + "" + BOLD + "Voting begins in:";
			case GAME_SELECTION:
			return RED + "" + BOLD + "Voting closes in:";
			case GAME_SELECTION_FINAL:
			case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT:
			return RED + "" + BOLD + "Game chosen in:";
			case GAME_SELECTED_AWAIT_TELEPORT:
			return RED + "" + BOLD + "Teleporting to Game in:";
			case WAITING:
			default:
				return "null";
		}
	}

	public Timer getCurrentTimer() {
		return currentTimer;
	}
}
