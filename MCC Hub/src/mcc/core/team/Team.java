package mcc.core.team;

import mcc.core.players.EventPlayer;
import org.bukkit.ChatColor;

import java.util.List;

public class Team {

    private List<EventPlayer> players;

    public List<EventPlayer> getPlayers() {
        return players;
    }

    public char getTeamSymbol() {
        return '#';
    }

    public String getTeamHexColor() {
        return "#FA3A7F";
    }

    public String getName() {
        return "Example Team";
    }

    public String buildFullText() {
        return String.format("%s %s%s", this.getTeamSymbol(), this.getTeamHexColor(), this.getName());
    }
}
