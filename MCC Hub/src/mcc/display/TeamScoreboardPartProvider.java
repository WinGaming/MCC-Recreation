package mcc.display;

import java.util.UUID;

import org.bukkit.ChatColor;

import mcc.stats.record.TeamTemplateRecord;
import mcc.teams.TeamManager;
import mcc.utils.Pair;

public class TeamScoreboardPartProvider implements ScoreboardPartProvider {

    private TeamManager teamManager;

    public TeamScoreboardPartProvider(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        TeamTemplateRecord template = this.teamManager.getTeam(viewer) == null ? null : this.teamManager.getTeam(viewer).getTemplate();
        String teamString = null;
        if (template == null) {
            teamString = ChatColor.RED + "None";
        } else {
            // We need to add reset, because minecraft hides names that start with #
            teamString = ChatColor.RESET + template.buildLongString();
        }

        return new Pair<>(new String[] {
                ChatColor.WHITE + "" + ChatColor.BOLD + "Your Team:",
                teamString
        }, 0L);
    }
    
}
