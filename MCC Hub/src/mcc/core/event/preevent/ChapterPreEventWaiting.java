package mcc.core.event.preevent;

import mcc.core.event.EventChapterState;

import java.util.Optional;

/**
 * This PreEvent-EventChapterState waits for an external signal to start the event.
 */
public class ChapterPreEventWaiting extends EventChapterState<ChapterPreEventState> {

    private boolean shouldStart;

    public void markShouldStart() {
        this.shouldStart = true;
    }

    @Override
    public Optional<ChapterPreEventState> tick(long now) {
        // This state can only be left using for example a minecraft command setting shouldStart to true
        return this.shouldStart ? Optional.of(ChapterPreEventState.CHECKING_READINESS) : Optional.empty();
    }
}
