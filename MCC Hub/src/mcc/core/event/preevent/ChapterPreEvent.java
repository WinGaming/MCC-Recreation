package mcc.core.event.preevent;

import mcc.core.EventChapterState;
import mcc.core.EventChapterStateFactory;
import mcc.core.event.EventChapter;

public class ChapterPreEvent extends EventChapter<ChapterPreEvent.ChapterPreEventStates> {

    public ChapterPreEvent() {
        super(ChapterPreEventStates.WAITING);
    }

    public enum ChapterPreEventStates implements EventChapterStateFactory<ChapterPreEventStates> {

        /** The event is starting, giving everyone time to prepare */
        WAITING,

        /** Asking all players if they are ready */
        CHECKING_READINESS,

        /** The countdown to the start of the event */
        EVENT_COUNTDOWN;

        @Override
        public EventChapterState<ChapterPreEventStates> create() {
            return null; // TODO: Implement (most likely using Class::new)
        }
    }
}
