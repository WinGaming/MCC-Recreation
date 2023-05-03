package mcc.stats.record;

import java.util.List;
import java.util.Map;

public class GameRecord {
    
    /** The type of the game */
    private String gameType; // TODO: Use enum?
    /** The id to find the log file. The log itself is not saved in the database */
    private String gameLogId;
    /** A map containing the coins each player earned in the game */
    private Map<String, Integer> playerCoins;
    /** A list of all teams who took part */
    private List<TeamTemplateRecord> teams;

}
