package mcc.display;

import mcc.utils.Pair;

import java.util.UUID;

public class StaticScoreboardPartProvider implements ScoreboardPartProvider {

    private final Pair<String[], Long> lines;

    public StaticScoreboardPartProvider(String... lines) {
        this.lines = new Pair<>(lines, System.currentTimeMillis());
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        return this.lines;
    }
}
