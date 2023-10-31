package mcc.core.event.preevent;

import mcc.core.event.EventChapterState;
import mcc.core.MCCEvent;

import java.util.Optional;

/**
 * This PreEvent-EventChapterState checks the readiness of all teams and starts the countdown to the event.
 */
public class ChapterPreEventCheckingReadiness extends EventChapterState<ChapterPreEventState> {

    @Override
    public void init() {
        MCCEvent.getInstance().getTeamManager().resetReadiness();
    }

    @Override
    public Optional<ChapterPreEventState> tick(long now) {
        return MCCEvent.getInstance().getTeamManager().allTeamsReady()
                ? Optional.of(ChapterPreEventState.EVENT_COUNTDOWN)
                : Optional.empty();
    }
}
