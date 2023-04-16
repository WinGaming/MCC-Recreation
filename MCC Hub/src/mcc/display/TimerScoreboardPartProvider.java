package mcc.display;

import java.util.UUID;

import mcc.utils.Pair;
import mcc.utils.Timer;

public class TimerScoreboardPartProvider implements ScoreboardPartProvider {

    private String title;
    private Timer timer;

    public TimerScoreboardPartProvider(String title, Timer timer) {
        this.title = title;
        this.timer = timer;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        return new Pair<>(new String[] { title, timer == null ? "Waiting..." : timer.buildText(System.currentTimeMillis()) }, System.currentTimeMillis());
    }
}
