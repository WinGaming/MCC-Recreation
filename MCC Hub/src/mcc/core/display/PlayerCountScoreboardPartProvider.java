package mcc.core.display;

import mcc.core.team.TeamManager;
import mcc.display.ScoreboardPartProvider;
import mcc.utils.Pair;
import static org.bukkit.ChatColor.*;

import java.util.UUID;

public class PlayerCountScoreboardPartProvider implements ScoreboardPartProvider {

    private final TeamManager teamManager;

    public PlayerCountScoreboardPartProvider(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        return new Pair<>(
                new String[] {
                        GREEN + "" + BOLD + "Players: " + RESET +  this.teamManager.getPlayerCount() + "/" + this.teamManager.getTotalPlayerCount()
                }, System.currentTimeMillis()
        );
    }
}
