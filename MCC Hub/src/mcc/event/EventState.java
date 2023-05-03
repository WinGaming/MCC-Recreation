package mcc.event;

public enum EventState {
    /** The game wasn't started yet */
    NOT_STARTED,
    /** The game is starting, give everyone time to prepare */
    STARTING,
    /** Time to go to the decision dome */
    DECISIONDOME_COUNTDOWN,
    /** The decision dome is running, and it manages everything on its own */
    DECISIONDOME_RUNNING,
}
