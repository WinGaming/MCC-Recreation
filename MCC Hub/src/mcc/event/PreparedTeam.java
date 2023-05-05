package mcc.event;

import java.util.List;
import java.util.UUID;

import mcc.stats.record.TeamTemplateRecord;

public class PreparedTeam {
    
    private String id;

    private List<UUID> players;
    private TeamTemplateRecord template;

    public PreparedTeam(String id, List<UUID> players, TeamTemplateRecord teamplate) {
        this.id = id;
        this.players = players;
        this.template = teamplate;
    }

    public String getId() {
        return id;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public TeamTemplateRecord getTemplate() {
        return template;
    }

    public String buildLongString() {
        return this.template.getColor() + "" + this.template.getIcon() + " " + this.template.getName();
    }
}
