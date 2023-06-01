package mcc.display;

import java.util.UUID;
import java.util.function.Supplier;

import mcc.timer.Timer;
import mcc.utils.Pair;

public class SuppliedTimerScoreboardPartProvider implements ScoreboardPartProvider {

    private Supplier<String> title;
    private Supplier<Timer> timer;

    public SuppliedTimerScoreboardPartProvider(Supplier<String> title, Supplier<Timer> timer) {
        this.title = title;
        this.timer = timer;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        Timer timer = this.timer.get();
        String title = this.title.get();
        return new Pair<>(new String[] {
            title == null ? "null" : title,
            timer == null ? "Waiting..." : timer.buildText(System.currentTimeMillis()) }, System.currentTimeMillis()
        );
    }
}
