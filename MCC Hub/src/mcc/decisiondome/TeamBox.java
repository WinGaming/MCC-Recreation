package mcc.decisiondome;

import org.bukkit.entity.Player;

import mcc.locationprovider.LocationProvider;
import mcc.teams.Team;

public class TeamBox {
    
    /** LocationProvider for spawn locations */
    private LocationProvider locationProvider;

    /** The team instance */
    private Team team;

    public TeamBox(Team team, LocationProvider spawnLocationProvider) {
        this.team = team;
        this.locationProvider = spawnLocationProvider;
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
    }

    /** Prevents the team from interacting with the vote (e.g. placing blocks). These actions can be undone with {@code resetBuild()} */
    public void disableVote() {
        // TODO: Impleement
    }
}
