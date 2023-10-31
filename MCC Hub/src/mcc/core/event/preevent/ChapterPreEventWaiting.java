package mcc.core.event.preevent;

import mcc.core.EventChapterState;

import java.util.Optional;

/**
 * This PreEvent-EventChapterState waits for an external signal to start the event.
 */
public class ChapterPreEventWaiting extends EventChapterState<ChapterPreEventState> {

    @Override
    public Optional<ChapterPreEventState> tick(long now) {
        // This state can only be left using for example a minecraft command
        return Optional.empty();
    }
}
