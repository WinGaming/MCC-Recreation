package mcc.game;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import mcc.event.Event;
import mcc.scores.CoinScore;
import mcc.timer.Timer;

public class BattleBox extends MCCGame<BattleBox.BattleBoxState, CoinScore, Integer> {

    public BattleBox(Event event) {
        super("Battle Box", BattleBoxState.PREPARE_ROUND, Bukkit.getWorld("world").getSpawnLocation(), event, TeamMatcher.ALL);
        // TODO: Load scripts
    }

    @Override
    public BattleBoxState onTimerEnd(BattleBoxState state) {
        switch (state) {
            case PREPARE_ROUND:
                return BattleBoxState.KIT_SELECTION;
            case KIT_SELECTION:
                return BattleBoxState.BATTLE_PREPARE;
            case BATTLE_PREPARE:
                return BattleBoxState.BATTLE;
            case BATTLE:
                return BattleBoxState.BATTLE_POST_TIMEOUT;
            case BATTLE_POST_TIMEOUT:
                return null;
        }

        return null;
    }

    @Override
    public Timer getTimer(BattleBoxState state) {
        switch (state) {
            case PREPARE_ROUND:
                return null;
            case KIT_SELECTION:
                return new Timer(TimeUnit.SECONDS, 20);
            case BATTLE_PREPARE:
                return new Timer(TimeUnit.SECONDS, 5);
            case BATTLE:
                return new Timer(TimeUnit.SECONDS, 60);
            case BATTLE_POST_TIMEOUT:
                return new Timer(TimeUnit.SECONDS, 10);
        }

        return null;
    }

    public enum BattleBoxState {
        /** Time to load things */
        PREPARE_ROUND,
        /** Allow teams to select a kit */
        KIT_SELECTION,
        /** Quick preparation for the actuall battle */
        BATTLE_PREPARE,
        /** The actual battle */
        BATTLE,
        /** A timeout to relax */
        BATTLE_POST_TIMEOUT,
    }
}
