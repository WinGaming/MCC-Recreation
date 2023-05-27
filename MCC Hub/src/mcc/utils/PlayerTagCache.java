package mcc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerTagCache {
    
    private static Map<UUID, List<String>> playerTags;

    static {
        playerTags = new HashMap<>();
    }

    public static void addTag(UUID player, String tag) {
        if (!playerTags.containsKey(player)) {
            playerTags.put(player, new ArrayList<>());
        }

        if (playerTags.get(player).contains(tag)) return;
        playerTags.get(player).add(tag);
    }

    public static void removeTag(UUID player, String tag) {
        if (!playerTags.containsKey(player)) return;
        playerTags.get(player).remove(tag);
    }

    public static boolean hasTag(UUID player, String tag) {
        if (!playerTags.containsKey(player)) return false;
        return playerTags.get(player).contains(tag);
    }

    public static void clearTags(UUID player) {
        playerTags.remove(player);
    }
}
