package mcc.decisiondome;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import mcc.MCCTest;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.utils.Vector3i;
import mcc.yml.HubDecisiondomeConfig;

public class DecisionDome {
	
	private DecisionField[] fields;
	
	private boolean validLoad = false;
	private boolean active = false;
	
	private Timer currentTimer;
	private DecisionDomeState state;
	
	private int ticksWaited = 0;
	private int currentSelectionIndex;
	
	private int chosenPosition = -1;
	
	private HubDecisiondomeConfig config;
	
	public DecisionDome(MCCTest pluginInstance, HubDecisiondomeConfig config) {
		this.config = config;
		
		Optional<String> fieldLoadError = this.reloadFieldsFromConfig();
		if (fieldLoadError.isPresent()) {
			this.validLoad = false;
			System.err.println(fieldLoadError.get());
		} else {
			this.validLoad = true;
		}
		
		this.state = DecisionDomeState.WAITING;
	}
	
	private Optional<String> reloadFieldsFromConfig() {
		if (this.active) {
			return Optional.of("Can't reload as decisiondome is currently active");
		}
		
		World world = Bukkit.getWorld(this.config.getWorldName());
		
		if (world == null) {
			return Optional.of("Failed to reload: Could not find world \"" + this.config.getWorldName() + "\"");
		}
		
		this.fields = new DecisionField[this.config.getFields().length];
		for (int i = 0; i < this.fields.length; i++) {
			Vector3i[] positions = this.config.getFields()[i].getPositions();
			Location[] locations = new Location[positions.length];
			
			for (int j = 0; j < positions.length; j++) {
				locations[j] = new Location(world, positions[j].getX(), positions[j].getY(), positions[j].getZ());
			}
			
			this.fields[i] = new DecisionField(locations, DecisionFieldState.ENABLED, this.config);
		}
		
		return Optional.empty();
	}

	private Timer createNewTimer(Pair<TimeUnit, Integer> timer) {
		return new Timer(timer.getA(), timer.getB());
	}
	
	private void setState(DecisionDomeState newState) {
		if (this.state != newState) {
			this.state = newState;
			switch (newState) {
			case WAITING: this.currentTimer = null; break;
			case GAME_SELECTION_INTRO: this.currentTimer = createNewTimer(config.getGameSelectionPreVoteTimer()); this.ticksWaited = 0; break;
			case GAME_SELECTION: this.currentTimer = createNewTimer(config.getGameSelectionTimer()); this.ticksWaited = 0; break;
			case GAME_SELECTION_FINAL: this.currentTimer = createNewTimer(config.getGameSelectionFinalTimer()); break;
			case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT: this.currentTimer = null; break;
			case GAME_SELECTED: this.currentTimer = createNewTimer(config.getGameSelectedTimer()); break;
			case GAME_SELECTED_AWAIT_TELEPORT: this.currentTimer = createNewTimer(config.getGameSelectedAwaitTeleportTimer()); break;
			}
			if (this.currentTimer != null) this.currentTimer.start(System.currentTimeMillis());
		}
	}
	
	public void start() {
		if (!this.validLoad) {
			System.err.println("Can not start decision dome, no valid configuration loaded");
		}
		
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
				
				Pair<TimeUnit, Integer> selectionFinalTimes = this.config.getGameSelectionFinalTimer();
				double selectionFinalTimer = selectionFinalTimes.getA().toMillis(selectionFinalTimes.getB());
				
				Pair<TimeUnit, Integer> selectionTimes = this.config.getGameSelectionTimer();
				double selectionTimer = selectionTimes.getA().toMillis(selectionTimes.getB());
				
				if (state == DecisionDomeState.GAME_SELECTION) totalRemaining += selectionFinalTimer;
				
				Bukkit.broadcastMessage("Total remaining: " + totalRemaining + " (" + selectionFinalTimer + " + " + selectionTimer + ")");
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
						this.setState(DecisionDomeState.GAME_SELECTED); // we continue the method here, because we want to show the correct highlighting
					}
				} else {
					this.ticksWaited++;
				}
			}
			
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
				case GAME_SELECTED:
				case GAME_SELECTED_AWAIT_TELEPORT:
					for (int i = 0; i < this.fields.length; i++) this.fields[i].setState(i == this.currentSelectionIndex ? DecisionFieldState.SELECTED : DecisionFieldState.ENABLED);
					break;
				}
			}
			if (this.currentTimer != null && this.currentTimer.remaining(System.currentTimeMillis()) <= 0) {
				switch (this.state) {
				case WAITING: System.err.println("A timer run out where it shouldn't!"); break;
				case GAME_SELECTION_INTRO: Bukkit.broadcastMessage("Finished GAME_SELECTION_INTRO"); setState(DecisionDomeState.GAME_SELECTION); break;
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

	public DecisionDomeState getState() {
		return state;
	}

	public Timer getCurrentTimer() {
		return currentTimer;
	}
	
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
