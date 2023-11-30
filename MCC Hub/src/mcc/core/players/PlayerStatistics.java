package mcc.core.players;

import java.util.Map;

public class PlayerStatistics {

    private Map<String, Integer> eventCoins;

    public PlayerStatistics(Map<String, Integer> eventCoins) {
        this.eventCoins = eventCoins;
    }

    public int getEventCoins(String eventId) {
        return this.eventCoins.getOrDefault(eventId, 0);
    }

    public int getTotalCoins() {
        return this.eventCoins.values().stream().reduce(0, Integer::sum);
    }
}
