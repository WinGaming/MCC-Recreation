package mcc.decisiondome;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.decisiondome.selector.FieldSelector;
import mcc.game.GameTask;
import mcc.locationprovider.LocationProvider;
import mcc.teams.Team;
import mcc.teams.TeamManager;
import mcc.utils.Vector3i;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.decisiondome.TeamBoxConfig;

public class DecisionDomeUtils {
    
	public static List<String> validateConfig(HubDecisiondomeConfig config) {
		List<String> errors = new ArrayList<>();

		if (config.getWorldName().equals("WORLD_NAME")) errors.add("world");
		if (config.getFields().length < 2) errors.add("fields.amount");
		if (config.getTeamBoxes().length < 2) errors.add("teamboxes.amount");

		return errors;
	}

	public static void sendConfigValidationResult(CommandSender player, List<String> errors) {
		player.sendMessage("== Found " + errors.size() + " errors ==");
		
		// TODO: Use language files and on hover explanations
		for (String errorKey : errors) {
			if (errorKey.equals("world")) {
				player.sendMessage("Invalid world");
			} else if (errorKey.equals("fields.amount")) {
				player.sendMessage(String.format("Not enough fields"));
				player.sendMessage(String.format("    Use /decisiondome fields add"));
			} else if (errorKey.equals("teamboxes.amount")) {
				player.sendMessage(String.format("Not enough teamboxes"));
				player.sendMessage(String.format("    Use /decisiondome teambox add"));
			} else {
				player.sendMessage(errorKey);
			}
		}

		player.sendMessage("");		
	}

	public static DecisionDome loadFromConfig(HubDecisiondomeConfig config, TeamManager teamManager, FieldSelector fieldSelector, GameTask gameTask) throws IllegalArgumentException {
		World world = Bukkit.getWorld(config.getWorldName());
		
		if (world == null) {
			throw new IllegalArgumentException("Failed to reload: Could not find world \"" + config.getWorldName() + "\"");
		}
		
		DecisionField[] fields = new DecisionField[config.getFields().length];
		for (int i = 0; i < fields.length; i++) {
			Vector3i[] positions = config.getFields()[i].getPositions();
			Location[] locations = new Location[positions.length];
			
			for (int j = 0; j < positions.length; j++) {
				locations[j] = new Location(world, positions[j].getX(), positions[j].getY(), positions[j].getZ());
			}
			
			fields[i] = new DecisionField(locations, DecisionFieldState.ENABLED, config);
		}
		
		List<Team> teams = teamManager.getTeams();
		if (teams.size() > config.getTeamBoxes().length) {
			throw new IllegalStateException("Not enough team boxes configurated: " + config.getTeamBoxes().length + " configurated, but " + teamManager.getTeamCount() + " needed");
		}

		TeamBox[] teamBoxes = new TeamBox[config.getTeamBoxes().length];
		for (int i = 0; i < teamBoxes.length; i++) {
			TeamBoxConfig boxConfig = config.getTeamBoxes()[i];
			Team team = i >= teams.size() ? null : teams.get(i);
			LocationProvider spawnProvider = boxConfig.getSpawnLocationProviderConfig().getProvider();
			teamBoxes[i] = new TeamBox(team, spawnProvider, boxConfig.getCornerA(), boxConfig.getCornerB());
		}

		return new DecisionDome(fields, config, teamBoxes, fieldSelector, gameTask);
	}
}
