package mcc.teams;

import java.util.List;

import org.bukkit.entity.Player;

public class Team {
    
    private TeamTemplate template;

    private List<Player> players;

    public Team(TeamTemplate template) {
        this.template = template;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public TeamTemplate getTemplate() {
        return template;
    }
}
