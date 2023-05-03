package mcc.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import mcc.stats.record.TeamTemplateRecord;

public class Team {
    
    private TeamTemplateRecord template;

    private List<Player> players;

    private List<String> tempNameVar;

    public Team(TeamTemplateRecord template) {
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

    public TeamTemplateRecord getTemplate() {
        return template;
    }
}
