package mcc.event;

import java.util.List;
import java.util.Optional;

import mcc.stats.EventStats;
import mcc.stats.record.EventRecord;
import mcc.teams.TeamManager;
import net.minecraft.network.chat.IChatBaseComponent;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.YELLOW;

public class EventFactory {

    public static Event fromStats(String eventId, EventStats stats) {
        Optional<List<PreparedTeam>> teams = stats.getTeamsForEvent(eventId);
        Optional<EventRecord> lastEvent = stats.getLastEventBefore(eventId);

        if (teams.isEmpty()) {
            throw new IllegalArgumentException("No teams found for event " + eventId);
        }

        // {"text":"MC Championship Pride 22","color":"yellow","bold":true}
        return new Event(eventId, lastEvent.isPresent() ? lastEvent.get().getEventId() : null,
                new TeamManager(teams.get()),
                IChatBaseComponent.literal(YELLOW + "" + BOLD + "MC Championship Pride 22"));
    }

}
