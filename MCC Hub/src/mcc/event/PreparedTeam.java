package mcc.event;

import java.util.List;
import java.util.UUID;

import mcc.stats.record.TeamTemplateRecord;

public class PreparedTeam {
    
    private List<UUID> players;
    private TeamTemplateRecord template;

    public PreparedTeam(List<UUID> players, TeamTemplateRecord teamplate) {
        this.players = players;
        this.template = teamplate;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public TeamTemplateRecord getTemplate() {
        return template;
    }
}
