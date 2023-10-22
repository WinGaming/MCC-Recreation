package mcc.core;

import mcc.core.event.EventChapter;

public abstract class EventChapterState<Chapter extends EventChapter> {

    private Chapter chapter;

    public EventChapterState(Chapter chapter) {
        this.chapter = chapter;
    }

    /**
     * Called when the state gets created.
     */
    public void init() {}

    public abstract boolean tick(long now);

    /**
     * Called when the state gets destroyed.
     * This for example happens when the state changes.
     */
    public void destroy() {}
}
