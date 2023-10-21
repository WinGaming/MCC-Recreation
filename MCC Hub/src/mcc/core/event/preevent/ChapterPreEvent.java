package mcc.core.event.preevent;

import mcc.core.event.EventChapter;

public class ChapterPreEvent extends EventChapter {

    private ChapterPreEventStates currentState;

    public enum ChapterPreEventStates {

        /** The event is starting, giving everyone time to prepare */
        WAITING,

        /** Asking all players if they are ready */
        CHECKING_READINESS,

        /** The countdown to the start of the event */
        EVENT_COUNTDOWN
    }
}
