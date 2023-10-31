package mcc.core.event.preevent;

import mcc.core.EventChapterState;

import java.util.Optional;

/**
 * This PreEvent-EventChapterState creates a countdown to start the event.
 */
public class ChapterPreEventEventCountdown extends EventChapterState<ChapterPreEventState> {

    private final long startTime;

    public ChapterPreEventEventCountdown() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public Optional<ChapterPreEventState> tick(long now) {
        return Optional.empty(); // TODO:
    }
}
