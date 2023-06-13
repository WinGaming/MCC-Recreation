package mcc.game.map;

import mcc.game.map.blueprint.MapBlueprint;

public class Map {
    
    private String name;
    private MapBlueprint blueprint;

    public Map(String name, MapBlueprint blueprint) {
        this.name = name;
        this.blueprint = blueprint;
    }

    public MapBlueprint getBlueprint() {
        return blueprint;
    }

    public String getName() {
        return name;
    }
}
