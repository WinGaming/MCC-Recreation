package mcc.decisiondome.selector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.BoundingBox;

import mcc.decisiondome.DecisionField;
import mcc.utils.Vector3d;

public class EntityFieldSelector implements FieldSelector {
    
    private static int upwardsTest = 5;

    @Override
    public int select(DecisionField[] fields) {
        if (fields.length == 0) return -1;

        Vector3d minPos = null;
        Vector3d maxPos = null;
        World world = null;

        Map<String, Integer> xzFieldIndexMap = new HashMap<>();
        
        for (int i = 0; i < fields.length; i++) {
            DecisionField field = fields[i];
            Location[] blocks = field.getBlockLocations();

            if (field.isDisabled()) continue;

            for (Location location : blocks) {
                if (world == null) world = location.getWorld();
                if (!world.getName().equals(location.getWorld().getName())) {
                    throw new IllegalArgumentException("DecisionField[] contains elements from different worlds");
                }

                if (minPos == null) {
                    minPos = new Vector3d(location.getX(), location.getY(), location.getZ());
                }

                if (location.getX() < minPos.getX()) minPos.setX(location.getX());
                if (location.getY() < minPos.getY()) minPos.setY(location.getY());
                if (location.getZ() < minPos.getZ()) minPos.setZ(location.getZ());

                if (maxPos == null) {
                    maxPos = new Vector3d(location.getX(), location.getY(), location.getZ());
                }

                if (location.getX() > maxPos.getX()) maxPos.setX(location.getX());
                if (location.getY() > maxPos.getY()) maxPos.setY(location.getY());
                if (location.getZ() > maxPos.getZ()) maxPos.setZ(location.getZ());

                String xzKey = location.getBlockX() + "#" + location.getBlockZ();
                if (xzFieldIndexMap.containsKey(xzKey)) {
                    throw new IllegalArgumentException("DecisionField[] either contains duplicate blocklocations");
                }
                xzFieldIndexMap.put(xzKey, i);
            }
        }

        if (world == null || minPos == null || maxPos == null) return -1;

        BoundingBox box = new BoundingBox(minPos.getX(), minPos.getY(), minPos.getZ(), maxPos.getX(), maxPos.getY() + EntityFieldSelector.upwardsTest, maxPos.getZ());
        Collection<Entity> possibleEntities = world.getNearbyEntities(box, entity -> {
            return entity.getType() == EntityType.CHICKEN; // TODO: Config
        });

        Map<Integer, Integer> fieldScores = new HashMap<>();
        for (Entity entity : possibleEntities) {
            String xzKey = entity.getLocation().getBlockX() + "#" + entity.getLocation().getBlockZ();
            if (xzFieldIndexMap.containsKey(xzKey)) {
                int fieldIndex = xzFieldIndexMap.get(xzKey);
                fieldScores.put(fieldIndex, fieldScores.getOrDefault(fieldIndex, 0) + 1);
            }
        }

        int result = -1;
        int lastScore = -1;
        for (Integer key : fieldScores.keySet()) {
            if (fieldScores.get(key) > lastScore) {
                lastScore = fieldScores.get(key);
                result = key;
            }
        }

        if (result == -1) return 0;

        return result;
    }
}
