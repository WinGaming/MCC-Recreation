package mcc.display;

import java.util.UUID;

import org.bukkit.ChatColor;

import mcc.teams.Team;
import mcc.teams.TeamManager;
import mcc.utils.Pair;

public class TeamScoreboardPartProvider implements ScoreboardPartProvider {

    private TeamManager teamManager;

    public TeamScoreboardPartProvider(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        Team team = this.teamManager.getTeam(viewer) == null ? null : this.teamManager.getTeam(viewer);
        String teamString = null;
        if (team == null) {
            teamString = ChatColor.RED + "None";
        } else {
            // We need to add reset, because minecraft hides names that start with #
            teamString = ChatColor.RESET + team.getTeam().buildLongString();
        }

        return new Pair<>(new String[] {
                ChatColor.WHITE + "" + ChatColor.BOLD + "Your Team:",
                teamString
        }, 0L);
    }
}
