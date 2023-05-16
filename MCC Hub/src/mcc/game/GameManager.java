package mcc.game;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GameManager {
    
    private static Map<String, Supplier<Game>> gameLibrary;

    static {
        gameLibrary = new HashMap<>();
    }

    public static boolean registerGame(String key, Supplier<Game> supplier) {
        if (gameLibrary.containsKey(key)) return false;
        
        gameLibrary.put(key, supplier);
        return true;
    }

    public static Game createGame(String key) {
        return gameLibrary.containsKey(key) ? gameLibrary.get(key).get() : null;
    }
}
