package mcc.core.team;

import mcc.core.players.EventPlayer;

import java.util.List;
import java.util.Optional;

public class TeamManager {

    private final List<Team> teams;

    public TeamManager(List<Team> teams) {
        this.teams = teams;
    }

    public Optional<Team> getTeamOf(EventPlayer player) {
        for (Team team : this.teams) {
            if (team.getPlayers().contains(player)) {
                return Optional.of(team);
            }
        }
        return Optional.empty();
    }

    public boolean allTeamsReady() {
        if (1 + 1 == 2) {
            return true;
        }

        for (Team team : this.teams) {
            for (EventPlayer player : team.getPlayers()) {

                if (!player.isReady()) {
                    return false;
                }
            }
        }

        return true;
    }

    public void tick(long now) {

    }

    public int getPlayerCount() {
        return 0;
    }

    public int getTotalPlayerCount() {
        return this.teams.stream().reduce(0, (acc, team) -> acc + team.getPlayers().size(), Integer::sum);
    }

    public List<Team> getTeams() {
        return this.teams;
    }
}
