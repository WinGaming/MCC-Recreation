package mcc.yml.event.scoreboardparts;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.bukkit.configuration.ConfigurationSection;

import mcc.display.ScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
import mcc.display.TeamScoreboardPartProvider;
import mcc.display.TeamsPlayerCountScoreboardPartProvider;
import mcc.event.Event;

public class ScoreboardPartsParser {
    
    private static final Map<String, BiFunction<Event, ConfigurationSection, ScoreboardPartProvider>> scoreboardPartProviderParsers;

    static {
        scoreboardPartProviderParsers = new HashMap<>();
        scoreboardPartProviderParsers.put("eventTimer", (event, config) -> {
            return new SuppliedTimerScoreboardPartProvider(event::getTimerTitle, event::getTimer);
        });
        scoreboardPartProviderParsers.put("teamPlayerCount", (event, config) -> {
            return new TeamsPlayerCountScoreboardPartProvider(event.getTeamManager());
        });
        scoreboardPartProviderParsers.put("team", (event, config) -> {
            return new TeamScoreboardPartProvider(event.getTeamManager());
        });
        scoreboardPartProviderParsers.put("tempCoins", (event, config) -> {
            return event.getTempCoinsProvider();
        });
    }

    public static ScoreboardPartProvider parseScoreboardPartProvider(String id, Event event, ConfigurationSection section) {
        return scoreboardPartProviderParsers.get(id).apply(event, section);
    }
}
