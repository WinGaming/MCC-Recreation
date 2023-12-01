package mcc.core.event.minigame;

import mcc.core.event.EventChapterState;
import mcc.core.event.EventChapterStateFactory;

import java.util.function.Supplier;

public enum ChapterMinigameEventState implements EventChapterStateFactory<ChapterMinigameEventState> {

    MINIGAME(ChapterMinigameMinigame::new);

    private final Supplier<EventChapterState<ChapterMinigameEventState>> factory;

    ChapterMinigameEventState(Supplier<EventChapterState<ChapterMinigameEventState>> factory) {
        this.factory = factory;
    }

    @Override
    public EventChapterState<ChapterMinigameEventState> create() {
        return this.factory.get();
    }

}
