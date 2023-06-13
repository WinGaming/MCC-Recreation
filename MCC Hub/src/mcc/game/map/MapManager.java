package mcc.game.map;

import java.util.ArrayList;
import java.util.List;

import mcc.game.map.blueprint.MapBlueprint;
import mcc.utils.Vector3i;

public class MapManager {
    
    // TODO: Allow different strategies or just make a more optimized one?
    public static List<Vector3i> calculateAreaPositions(MapBlueprint blueprint, int amount) {
        List<Vector3i> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            result.add(new Vector3i(i * (blueprint.getSize().getX() + 1), 0, 0));
        }

        return result;
    }
}
