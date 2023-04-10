package mcc.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Team {
    
    private TeamTemplate template;

    private List<Player> players;

    private List<String> tempNameVar;

    public Team(TeamTemplate template) {
        this.template = template;
        this.tempNameVar = new ArrayList<>();
    }

    public List<String> getTempNameVar() {
        return tempNameVar;
    }
    
    public void addTempNameVar(String name) {
        tempNameVar.add(name);
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
