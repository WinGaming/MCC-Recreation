package mcc.core.event.preevent;

import mcc.core.Component;
import mcc.core.event.EventChapter;

public class ChapterPreEvent extends EventChapter<ChapterPreEventState> {

    public ChapterPreEvent() {
        super(ChapterPreEventState.WAITING);
    }

    @Override
    public Component[] createComponents() {
        return new Component[] {

        }; // TODO: Load from config?
    }
}
