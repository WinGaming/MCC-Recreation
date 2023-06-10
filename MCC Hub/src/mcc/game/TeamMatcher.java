package mcc.game;

import java.util.ArrayList;
import java.util.List;

import mcc.teams.Team;
import mcc.teams.TeamManager;

@FunctionalInterface
public interface TeamMatcher {

    public List<List<Team[]>> generateMatches(TeamManager teamManager);

    public static TeamMatcher ALL = (TeamManager teamManager) -> {
        List<List<Team[]>> matches = new ArrayList<>();

        int rounds = teamManager.getTeamCount(); // Each team will play each other team once
        List<Team> teams = teamManager.getTeams();
        for (int i = 0; i < rounds; i++) {
            matches.add(new ArrayList<>());
            for (int j = 0; j < teams.size(); j++) {
                Team team1 = teams.get(j);
                Team team2 = teams.get((j + i) % teams.size());
                matches.get(i).add(new Team[] { team1, team2 });
            }
        }

        return matches;
    };
}
