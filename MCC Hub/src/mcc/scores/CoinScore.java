package mcc.scores;

import java.util.Optional;

public class CoinScore implements Score<CoinScore, Integer> {

    private int coins;

    @Override
    public int compareTo(CoinScore o) {
        return Integer.compare(coins, o.coins);
    }

    @Override
    public void setScore(Integer value) {
        this.coins = value;
    }

    @Override
    public Integer getScore() {
        return coins;
    }

    @Override
    public String toChatText(Optional<CoinScore> viewerScore) {
        return this.coins + "#";
    }

    @Override
    public String toScoreboardText(Optional<CoinScore> viewerScore) {
        return this.coins + "#";
    }
}
