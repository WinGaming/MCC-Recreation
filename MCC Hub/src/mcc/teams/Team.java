package mcc.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import mcc.event.PreparedTeam;
import mcc.stats.record.TeamTemplateRecord;

public class Team {

    private final PreparedTeam team;

    private List<Player> players;

    public Team(PreparedTeam team) {
        this.team = team;

        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public TeamTemplateRecord getTemplate() {
        return this.team.getTemplate();
    }

    public PreparedTeam getTeam() {
        return team;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
