package mcc.stats.record;

import java.util.List;

public class EventRecord {
    
    /** Timestamp when the event started */
    private long startTime;

    /** Unique id of the event */
    private String eventId;

    /** A map containing a sorted (!) list of the games played */
    private List<GameRecord> games;
    /** The id to find the log file. The log itself is not saved in the database */
    private String eventLogId;

    public EventRecord(long startTime, String eventId, List<GameRecord> games, String eventLogId) {
        this.startTime = startTime;
        this.eventId = eventId;
        this.games = games;
        this.eventLogId = eventLogId;
    }

    public String getEventId() {
        return eventId;
    }
}
