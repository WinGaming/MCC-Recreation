package mcc.core;

import mcc.core.event.EventChapter;

/**
 * This class represents the current event.
 * It works like a state machine, where the current chapter is the current state.
 */
public class MCCEvent {

    // Accessories, etc. are stored here
    // Basically anything that must be saved between chapters

    private EventChapter currentChapter;

    // TODO: Load components from config
    // TODO: Store ComponentContainer somwhow next to the EventChapter instance

}
