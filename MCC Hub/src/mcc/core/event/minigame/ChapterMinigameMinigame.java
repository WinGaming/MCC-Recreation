package mcc.core.event.minigame;

import mcc.core.event.EventChapterState;
import mcc.core.minigame.battlebox.BattleBoxRound;

import java.util.Optional;

public class ChapterMinigameMinigame extends EventChapterState<ChapterMinigameEventState> {

    private BattleBoxRound gameRound;

    public ChapterMinigameMinigame() {
        super();
        this.gameRound = new BattleBoxRound();
    }

    @Override
    public Optional<ChapterMinigameEventState> tick(long now) {
        this.gameRound.tick(now);

        return Optional.empty();
    }
}
