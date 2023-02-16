package mcc.decisiondome;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import mcc.MCCTest;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.utils.Timer;

public class DecisionDome {
	
	private DecisionField[] fields;
	
	private boolean active = false;
	
	private Timer currentTimer;
	private DecisionDomeState state;
	
	private int ticksWaited = 0;
	private int currentSelectionIndex;
	
	private int chosenPosition = -1;
	
	public DecisionDome(MCCTest pluginInstance, DecisionDomeTemplate template) {
		this.loadFromTemplate(template);
		
		this.state = DecisionDomeState.WAITING;
	}
	
	public boolean loadFromTemplate(DecisionDomeTemplate template) {
		if (this.active) {
			return false;
		}
		
		this.fields = new DecisionField[template.getFields().size()];
		for (int i = 0; i < this.fields.length; i++) {
			this.fields[i] = new DecisionField(template.getFields().get(i), DecisionFieldState.ENABLED);
		}
		
		return true;
	}

	private void setState(DecisionDomeState newState) {
		if (this.state != newState) {
			this.state = newState;
			switch (newState) {
			case WAITING: this.currentTimer = null; break;
			case GAME_SELECTION: this.currentTimer = new Timer(TimeUnit.SECONDS, 30); this.ticksWaited = 0; break;
			case GAME_SELECTION_FINAL: this.currentTimer = new Timer(TimeUnit.SECONDS, 5); break;
			case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT: this.currentTimer = null; break;
			case GAME_SELECTED: this.currentTimer = new Timer(TimeUnit.SECONDS, 10); break;
			case GAME_SELECTED_AWAIT_TELEPORT: this.currentTimer = new Timer(TimeUnit.HOURS, 10); break;
			}
			if (this.currentTimer != null) this.currentTimer.start(System.currentTimeMillis());
		}
	}
	
	public void start() {
		if (this.state == DecisionDomeState.WAITING) {
			this.setState(DecisionDomeState.GAME_SELECTION);
			this.active = true;
		} else {
			System.err.println("Can not start decision dome, it's already running!");
		}
	}
	
	public void tick() {
		if (this.active) {
			if (this.state == DecisionDomeState.GAME_SELECTION || this.state == DecisionDomeState.GAME_SELECTION_FINAL) {
				double totalRemaining = (double) this.currentTimer.remaining(System.currentTimeMillis());
				if (state == DecisionDomeState.GAME_SELECTION) totalRemaining += 5d * 1000d;
				double percentage = totalRemaining / ((30d + 5d) * 1000d);
				double minDelay = 2; // in ticks
				double maxAddedDelay = 20; // in ticks
				int ticksToWait = (int) Math.ceil(minDelay + maxAddedDelay * percentage);
				if (this.ticksWaited >= ticksToWait) {
					this.currentSelectionIndex++;
					this.currentSelectionIndex %= this.fields.length;
					this.ticksWaited = 0;
				} else {
					this.ticksWaited++;
				}
			} else if (this.state == DecisionDomeState.GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT) {
				double delay = 2; // minDelay
				int ticksToWait = (int) delay;
				if (this.ticksWaited >= ticksToWait) {
					this.currentSelectionIndex++;
					this.currentSelectionIndex %= this.fields.length;
					this.ticksWaited = 0;
					
					if (this.currentSelectionIndex == this.chosenPosition) {
						this.setState(DecisionDomeState.GAME_SELECTED); // we continue the method here, because we want to show the correct highlighting
					}
				} else {
					this.ticksWaited++;
				}
			}
			
			if (this.currentTimer != null || this.state.shouldUpdateFieldsWithoutActiveTimer()) {
				if (this.currentTimer != null) Bukkit.broadcastMessage(this.currentTimer.buildText(System.currentTimeMillis()));
				
				switch (this.state) {
				case WAITING:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(DecisionFieldState.DISABLED);
					break;
				case GAME_SELECTION:
				case GAME_SELECTION_FINAL:
				case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.HIGHLIGHTED : DecisionFieldState.ENABLED);
					break;
				case GAME_SELECTED:
				case GAME_SELECTED_AWAIT_TELEPORT:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.SELECTED : DecisionFieldState.ENABLED);
					break;
				}
			}
			if (this.currentTimer != null && this.currentTimer.remaining(System.currentTimeMillis()) <= 0) {
				switch (this.state) {
				case WAITING: System.err.println("A timer run out where it shouldn't!"); break;
				case GAME_SELECTION: Bukkit.broadcastMessage("Finished GAME_SELECTION"); setState(DecisionDomeState.GAME_SELECTION_FINAL); break;
				case GAME_SELECTION_FINAL: Bukkit.broadcastMessage("Finished GAME_SELECTION_FINAL"); setState(DecisionDomeState.GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT); break;
				case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT: System.err.println("A timer run out where it shouldn't!"); break;
				case GAME_SELECTED: Bukkit.broadcastMessage("Finished GAME_SELECTED"); setState(DecisionDomeState.GAME_SELECTED_AWAIT_TELEPORT); break;
				case GAME_SELECTED_AWAIT_TELEPORT: Bukkit.broadcastMessage("Finished GAME_SELECTED_AWAIT_TELEPORT"); setState(DecisionDomeState.WAITING); break;
				}
			}
			
			for (DecisionField field : this.fields) {
				field.tick();
			}
		}
	}
	
	private enum DecisionDomeState {
		// Paused or any other state where no countdown is active
		WAITING,
		// Teams can use items to change the result
		GAME_SELECTION,
		// Teams can't use any items anymore
		GAME_SELECTION_FINAL,
		// Waiting for selected field to reach chosen
		GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT(true),
		// A game was selected
		GAME_SELECTED,
		// A game was selected and players should teleport currently or have been teleported
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
	
//	private class SelectableGame {
//		
//		private final Game game;
//		
//		private boolean disabled;
//		
//		public SelectableGame(Game game, boolean disabled) {
//			this.game = game;
//			this.disabled = disabled;
//		}
//		
//		public Game getGame() {
//			return game;
//		}
//		
//		public void setDisabled(boolean disabled) {
//			this.disabled = disabled;
//		}
//		
//		public boolean isDisabled() {
//			return disabled;
//		}
//	}
}
