package mcc.stats;

import java.util.Optional;

import mcc.stats.record.EventRecord;

public interface EventStats {
    
    /** Gets and returns the last event started before {@code timestamp} */
    public Optional<EventRecord> getLastEventBefore(long timestamp);

}
