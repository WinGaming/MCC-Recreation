package mcc.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mcc.event.PreparedTeam;

public class TeamManager {
    
    private List<Team> teams;

    public TeamManager(Map<String, PreparedTeam> preparedTeams) {
        this.teams = new ArrayList<>();

        // // TODO: Load teams from file
        // this.teams.add(new Team(new TeamTemplateRecord("Golden Gold", ChatColor.GOLD, '#')));
        // this.teams.add(new Team(new TeamTemplateRecord("Ironing Iron", ChatColor.GRAY, '#')));
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
