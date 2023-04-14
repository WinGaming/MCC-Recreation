package mcc.display;

import java.util.UUID;

import mcc.utils.Timer;

public class TimerScoreboardPartProvider implements ScoreboardPartProvider {

    private String title;
    private Timer timer;

    public TimerScoreboardPartProvider(String title, Timer timer) {
        this.title = title;
        this.timer = timer;
    }

    @Override
    public String[] getLines(UUID viewer) {
        return new String[] { title, timer == null ? "Waiting..." : timer.buildText(System.currentTimeMillis()) };
    }
}
