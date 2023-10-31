package mcc.core.event.preevent;

import mcc.core.event.EventChapterState;
import mcc.core.event.EventChapterStateFactory;

import java.util.function.Supplier;

/**
 * The states that the pre-event chapter can be in
 */
public enum ChapterPreEventState implements EventChapterStateFactory<ChapterPreEventState> {

    /**
     * The event is starting, giving everyone time to prepare
     */
    WAITING(ChapterPreEventWaiting::new),

    /**
     * Asking all players if they are ready
     */
    CHECKING_READINESS(ChapterPreEventCheckingReadiness::new),

    /**
     * The countdown to the start of the event
     */
    EVENT_COUNTDOWN(ChapterPreEventEventCountdown::new);

    private final Supplier<EventChapterState<ChapterPreEventState>> factory;

    ChapterPreEventState(Supplier<EventChapterState<ChapterPreEventState>> factory) {
        this.factory = factory;
    }

    @Override
    public EventChapterState<ChapterPreEventState> create() {
        return this.factory.get();
    }
}
