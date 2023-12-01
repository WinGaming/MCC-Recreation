package mcc.core.minigame.battlebox;

import mcc.core.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BattleBoxDual {

    private Team teamA;
    private Team teamB;

    Location kitRoomA, kitRoomB, entranceA, entranceB;

    public BattleBoxDual(Team teamA, Team teamB) {
        this.teamA = teamA;
        this.teamB = teamB;

        kitRoomA = new Location(Bukkit.getWorld("world"), 270, 87, 115);
        kitRoomB = new Location(Bukkit.getWorld("world"), 270, 87, 78);
        entranceA = new Location(Bukkit.getWorld("world"), 270, 71, 115);
        entranceB = new Location(Bukkit.getWorld("world"), 270, 71, 78);
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Location getKitRoomA() {
        return kitRoomA;
    }

    public Location getKitRoomB() {
        return kitRoomB;
    }

    public Location getEntranceA() {
        return entranceA;
    }

    public Location getEntranceB() {
        return entranceB;
    }
}
