package mcc.core.team;

import mcc.core.players.EventPlayer;

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
}
