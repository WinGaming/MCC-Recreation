package mcc.game;

import org.bukkit.Location;

public class Map {
    
    private Location lobbyLocation;
    private String name;

    public Map(String name, Location lobbyLocation) {
        this.name = name;
        this.lobbyLocation = lobbyLocation;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public String getName() {
        return name;
    }
}
