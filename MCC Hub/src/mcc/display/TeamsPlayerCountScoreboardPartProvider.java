package mcc.display;

import java.util.UUID;

import org.bukkit.ChatColor;

import mcc.teams.TeamManager;
import mcc.utils.Pair;

public class TeamsPlayerCountScoreboardPartProvider implements ScoreboardPartProvider {

    private TeamManager teamManager;

    public TeamsPlayerCountScoreboardPartProvider(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        return new Pair<>(
            new String[] {
                ChatColor.GREEN + "" + ChatColor.BOLD + "Players: " + ChatColor.RESET +  this.teamManager.getPlayersCount() + "/" + this.teamManager.getTotalPlayersCount()
            }, System.currentTimeMillis()
        );
    }
}
