package mcc.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import mcc.event.Event;

public class GameManager {
    
    private static Map<String, Function<Event, Game>> gameLibrary;

    static {
        gameLibrary = new HashMap<>();

        registerGame("battlebox", BattleBox::new);
    }

    public static boolean registerGame(String key, Function<Event, Game> supplier) {
        if (gameLibrary.containsKey(key)) return false;
        
        gameLibrary.put(key, supplier);
        return true;
    }

    public static Game createGame(String key, Event event) {
        return gameLibrary.containsKey(key) ? gameLibrary.get(key).apply(event) : null;
    }

    public static List<String> getGameList() {
        return List.copyOf(gameLibrary.keySet());
    }
}
