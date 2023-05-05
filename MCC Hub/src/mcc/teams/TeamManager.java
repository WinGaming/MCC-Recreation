package mcc.teams;

import java.util.List;
import java.util.UUID;

import mcc.event.PreparedTeam;

public class TeamManager {
    
    private List<Team> teams;

    public TeamManager(List<PreparedTeam> preparedTeams) {
        this.teams = preparedTeams.stream().map(Team::new).toList();
    }

    public Team getTeam(UUID player) {
        for (Team team : this.teams) {
            if (team.getTeam().getPlayers().contains(player)) return team;
        }

        return null;
    }

    /** Gets the total number of currently online players in all teams */
    public int getPlayersCount() {
        return this.teams.stream().mapToInt(team -> team.getPlayers().size()).sum();
    }

    /** Gets the total number of all registered players that could join */
    public int getTotalPlayersCount() {
        return this.teams.stream().mapToInt(team -> team.getTeam().getPlayers().size()).sum();
    }
}
