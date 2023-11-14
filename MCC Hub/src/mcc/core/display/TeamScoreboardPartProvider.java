package mcc.core.display;

import mcc.core.BukkitConnector;
import mcc.core.players.EventPlayer;
import mcc.core.team.Team;
import mcc.core.team.TeamManager;
import mcc.display.ScoreboardPartProvider;
import mcc.utils.Pair;
import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.*;

import java.util.Optional;
import java.util.UUID;

public class TeamScoreboardPartProvider implements ScoreboardPartProvider {

    private TeamManager teamManager;
    private BukkitConnector bukkitConnector;

    public TeamScoreboardPartProvider(TeamManager teamManager, BukkitConnector bukkitConnector) {
        this.teamManager = teamManager;
        this.bukkitConnector = bukkitConnector;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        EventPlayer eventPlayer = this.bukkitConnector.getEventPlayer(viewer);
        Optional<Team> team = this.teamManager.getTeamOf(eventPlayer);
        Optional<String> teamName = team.map(Team::getName);

        // We need to add reset, because minecraft hides names that start with #
        return new Pair<>(new String[] {
                WHITE + "" + BOLD + "Your Team:",
                teamName.orElse("\uE000" + RED + " None"),
        }, 0L);
    }
}
