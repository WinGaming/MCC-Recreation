package mcc.decisiondome;

import org.bukkit.entity.Player;

import mcc.locationprovider.LocationProvider;
import mcc.teams.Team;
import mcc.utils.Vector3i;

public class TeamBox {

    private Vector3i cornerA, cornerB;
    
    /** LocationProvider for spawn locations */
    private LocationProvider locationProvider;

    /** The team instance */
    private Team team;

    public TeamBox(Team team, LocationProvider spawnLocationProvider, Vector3i cornerA, Vector3i cornerB) {
        this.team = team;
        this.locationProvider = spawnLocationProvider;

        this.cornerA = cornerA;
        this.cornerB = cornerB;
    }

    /** Teleports all players of the team into the box */
    public void teleportPlayers() {
        for (Player player : this.team.getPlayers()) {
            player.teleport(this.locationProvider.next());
        }
    }

    /** Resets blocks etc. for next round (e.g. closing blocks after box got disabled) */
    public void resetBuild() {
        // TODO: Implement
        // TODO: If team is null, the box is disabled, hide or "gray-out" the box
    }

    /** Prevents the team from interacting with the vote (e.g. placing blocks). These actions can be undone with {@code resetBuild()} */
    public void disableVote() {
        // TODO: Impleement
    }
}