package mcc.core.event;

import mcc.core.Component;

/**
 * This class represents a chapter of an event.
 */
public abstract class EventChapter {

    /**
     * Called when the {@link EventChapter} instance gets created.
     */
    public void init() {}

    /**
     * Called when the {@link EventChapter} instance gets destroyed.
     * This for example happens when the chapter changes.
     */
    public void destroy() {}

    /**
     * Creates and returns all components that should be used in this chapter.
     * @return A list of components
     */
    public Component[] createComponents() {
        return new Component[0];
    }
}
