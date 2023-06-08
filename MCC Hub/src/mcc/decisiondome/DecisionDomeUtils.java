package mcc.decisiondome;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import mcc.ExampleDecisionFieldDisplay;
import mcc.decisiondome.DecisionField.DecisionFieldState;
import mcc.decisiondome.selector.FieldSelector;
import mcc.event.Event;
import mcc.game.GameTask;
import mcc.locationprovider.LocationProvider;
import mcc.teams.Team;
import mcc.teams.TeamManager;
import mcc.utils.Vector3i;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.decisiondome.TeamBoxConfig;

public class DecisionDomeUtils {

	public static DecisionDome loadFromConfig(Event event, HubDecisiondomeConfig config, TeamManager teamManager, FieldSelector fieldSelector, GameTask gameTask) throws IllegalArgumentException {
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
			fields[i].getDisplay().reset();
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

		return new DecisionDome(event, fields, config, teamBoxes, fieldSelector, gameTask);
	}
}
