package mcc.core.event;

import mcc.core.Component;
import mcc.core.EventChapterState;
import mcc.core.EventChapterStateFactory;

import java.util.Optional;

/**
 * This class represents a chapter of an event.
 */
public abstract class EventChapter<StateEnum extends Enum<StateEnum> & EventChapterStateFactory<StateEnum>> {

    private StateEnum currentState;
    private EventChapterState<StateEnum> currentStateInstance;

    public EventChapter(StateEnum initialState) {
        this.currentState = initialState;
        this.currentStateInstance = initialState.create();
    }

    /**
     * Called when the chapter gets ticked.
     * @param now The current time in milliseconds
     */
    public void tick(long now) {
        Optional<StateEnum> newState = this.currentStateInstance.tick(now);
        if (newState.isPresent()) {
            this.currentStateInstance.destroy();
            this.currentState = newState.get();
            this.currentStateInstance = this.currentState.create();
        }
    }

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
