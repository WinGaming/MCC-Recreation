package mcc.stats;

import java.util.List;
import java.util.Optional;

import mcc.event.PreparedTeam;
import mcc.stats.record.EventRecord;

public interface EventStats {
    
    /** Gets and returns the last event before {@code eventId} */
    public Optional<EventRecord> getLastEventBefore(String eventId);

    /** Gets and returns all teams related to the event */
    public Optional<List<PreparedTeam>> getTeamsForEvent(String eventId);
}
