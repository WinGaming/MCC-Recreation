package mcc.core.event.none;

import mcc.core.event.EventChapterState;
import mcc.core.event.EventChapterStateFactory;

public enum ChapterNoneState implements EventChapterStateFactory<ChapterNoneState> {
    ;

    @Override
    public EventChapterState<ChapterNoneState> create() {
        return null;
    }
}
