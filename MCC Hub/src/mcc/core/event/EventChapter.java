package mcc.core.event;

import mcc.core.ComponentContainer;

/**
 * This class represents a chapter of an event.
 */
public abstract class EventChapter {

    /**
     * Called when the {@link EventChapter} instance gets created.
     */
    void init() {}

    /**
     * Called when the {@link EventChapter} instance gets destroyed.
     * This for example happens when the chapter changes.
     */
    void destroy() {}

}
