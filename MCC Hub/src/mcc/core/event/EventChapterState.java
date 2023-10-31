package mcc.core.event;

import mcc.core.event.EventChapter;

import java.util.Optional;

public abstract class EventChapterState<StateEnum extends Enum<StateEnum>> {

    /**
     * Called when the state gets created.
     */
    public void init() {}

    /**
     * Called when the state gets ticked.
     * @param now The current time in milliseconds
     * @return The next state, if should be changed
     */
    public abstract Optional<StateEnum> tick(long now); // TODO: Allow switching to different chapter
    // Maybe using the destroy method?

    /**
     * Called when the state gets destroyed.
     * This for example happens when the state changes.
     */
    public void destroy() {}
}
