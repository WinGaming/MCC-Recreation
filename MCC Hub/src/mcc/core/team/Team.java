package mcc.core.team;

import mcc.core.players.EventPlayer;

import java.util.List;

public class Team {

    private final List<EventPlayer> players;
    private final char teamSymbol;
    private final String teamColor;
    private final String name;

    public Team(String name, String hexColor, char symbol, List<EventPlayer> players) {
        this.name = name;
        this.teamColor = hexColor;
        this.teamSymbol = symbol;
        this.players = players;
    }

    public List<EventPlayer> getPlayers() {
        return players;
    }

    public char getTeamSymbol() {
        return this.teamSymbol;
    }

    public String getTeamHexColor() {
        return this.teamColor;
    }

    public String getName() {
        return this.name;
    }

    public String buildFullText() {
        return String.format("%s %s%s", this.getTeamSymbol(), this.getTeamHexColor(), this.getName());
    }
}
