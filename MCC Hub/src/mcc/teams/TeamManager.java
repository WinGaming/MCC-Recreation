package mcc.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import mcc.event.PreparedTeam;

public class TeamManager {
    
    private List<Team> teams;

    public TeamManager(List<PreparedTeam> preparedTeams) {
        this.teams = new ArrayList<>();
        for (PreparedTeam preparedTeam : preparedTeams) {
            this.teams.add(new Team(preparedTeam));
        }
    }

    public boolean joinIfRegistered(Player player) {
        for (Team team : this.teams) {
            if (team.getPlayers().contains(player)) return true;
            if (team.getTeam().getPlayers().contains(player.getUniqueId())) {
                team.addPlayer(player);
                return true;
            }
        }

        return false;
    }

    public Team getTeam(UUID player) {
        for (Team team : this.teams) {
            if (team.getTeam().getPlayers().contains(player)) return team;
        }

        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public int getTeamCount() {
        return this.teams.size();
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
