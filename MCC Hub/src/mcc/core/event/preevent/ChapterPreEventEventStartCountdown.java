package mcc.core.event.preevent;

import mcc.core.event.EventChapterState;
import mcc.timer.Timer;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This PreEvent-EventChapterState creates a countdown to start the event.
 */
public class ChapterPreEventEventStartCountdown extends EventChapterState<ChapterPreEventState> {

    private final Timer timer;

    public ChapterPreEventEventStartCountdown() {
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
        return this.timer.remaining(now) <= 0 ? Optional.of(ChapterPreEventState.EVENT_DECISION_DOME_COUNTDOWN) : Optional.empty();
    }
}
