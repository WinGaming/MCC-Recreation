package mcc.decisiondome;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.yml.hub.HubDecisiondomeConfig;

public class DecisionDome {
	
	private final DecisionField[] fields;
	private final HubDecisiondomeConfig config;
	
	private boolean active = false;
	
	private Timer currentTimer;
	private DecisionDomeState state;
	
	private int ticksWaited = 0;
	private int currentSelectionIndex;
	
	private int chosenPosition = -1;

	private TeamBox[] teamBoxes;
	
	protected DecisionDome(DecisionField[] fields, HubDecisiondomeConfig config, TeamBox[] teamBoxes) {
		this.config = config;
		this.fields = fields;
		this.teamBoxes = teamBoxes;
		this.state = DecisionDomeState.WAITING;
	}
	
	private void setState(DecisionDomeState newState) {
		if (this.state != newState) {
			this.state = newState;
			switch (newState) {
			case WAITING: this.currentTimer = null; break;
			case GAME_SELECTION_INTRO: this.currentTimer = Timer.fromPair(config.getGameSelectionPreVoteTimer()); this.ticksWaited = 0; break;
			case GAME_SELECTION: this.currentTimer = Timer.fromPair(config.getGameSelectionTimer()); this.ticksWaited = 0; break;
			case GAME_SELECTION_FINAL: this.currentTimer = Timer.fromPair(config.getGameSelectionFinalTimer()); break;
			case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT: this.currentTimer = null; break;
			case GAME_SELECTED: this.currentTimer = Timer.fromPair(config.getGameSelectedTimer()); break;
			case GAME_SELECTED_AWAIT_TELEPORT: this.currentTimer = Timer.fromPair(config.getGameSelectedAwaitTeleportTimer()); break;
			}
			if (this.currentTimer != null) this.currentTimer.start(System.currentTimeMillis());
		}
	}
	
	public void start() {
		// Teleporting all players...
		for (TeamBox box : this.teamBoxes) {
			box.teleportPlayers();
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

	public String getTimerTitle() {
		switch (this.state) {
			case GAME_SELECTION_INTRO:
			return RED + "" + BOLD + "Voting begins in:";
			case GAME_SELECTION:
			return RED + "" + BOLD + "Voting closes in:";
			case GAME_SELECTION_FINAL:
			case GAME_SELECTION_AWAIT_CHOSEN_POSITION_HIGHLIGHT:
			case GAME_SELECTED:
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
