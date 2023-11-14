package mcc.core.event.preevent;

import mcc.core.event.EventChapterState;
import mcc.timer.Timer;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This PreEvent-EventChapterState creates a countdown to start the event.
 */
public class ChapterPreEventEventDecisionDomeCountdown extends EventChapterState<ChapterPreEventState> {

    private final Timer timer;

    public ChapterPreEventEventDecisionDomeCountdown() {
        this.timer = new Timer(TimeUnit.SECONDS, 120);
    }

    @Override
    public void init() {
        this.timer.start(System.currentTimeMillis());
    }

    protected Timer getTimer() {
        return timer;
    }

    @Override
    public Optional<ChapterPreEventState> tick(long now) {
        return Optional.empty(); // We don't switch here, we do that in ChapterPreEvent
    }
}
