package mcc.event;

public enum EventState {
    /** The event wasn't started yet */
    NOT_STARTED,
    /** The event is starting, give everyone time to prepare */
    STARTING,
    /** Time to go to the decision dome */
    DECISIONDOME_COUNTDOWN,
    /** The decision dome is running, and it manages everything on its own */
    DECISIONDOME_RUNNING,
    /** A minigame is running */
    MINIGAME,

    /** The event is paused */
    PAUSED,
    /** The event is paused, while a minigame is running */
    PAUSED_IN_MINIGAME,
}
