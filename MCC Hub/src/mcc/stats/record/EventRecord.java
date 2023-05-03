package mcc.stats.record;

import java.util.List;

public class EventRecord {
    
    /** A map containing a sorted (!) list of the games played */
    private List<GameRecord> games;
    /** The id to find the log file. The log itself is not saved in the database */
    private String eventLogId;

}
