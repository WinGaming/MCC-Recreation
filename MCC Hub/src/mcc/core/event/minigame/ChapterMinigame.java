package mcc.core.event.minigame;

import mcc.core.event.EventChapter;
import mcc.core.event.preevent.ChapterPreEventState;

public class ChapterMinigame extends EventChapter<ChapterMinigameEventState> {

    public ChapterMinigame() {
        super(ChapterMinigameEventState.MINIGAME);
    }
}
