package mcc.core.minigame.battlebox;

import mcc.display.ScoreboardPartProvider;
import mcc.utils.Pair;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

public class BattleBoxScoreboard implements ScoreboardPartProvider {

    private BattleBoxRound gameRound;

    public BattleBoxScoreboard(BattleBoxRound gameRound) {
        this.gameRound = gameRound;
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        return new Pair<>(new String[] {
                ChatColor.AQUA + "" + ChatColor.BOLD + "Game ?/?: " + ChatColor.RESET + "Battle Box",
                ChatColor.AQUA + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + "Map Name",
                ChatColor.GREEN + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "" + 1 + "/" + '∞',
                (ChatColor.RED + "" + ChatColor.BOLD) + (gameRound.getTimer() == null ? gameRound.getTimerName() : (gameRound.getTimerName() + " " + ChatColor.RESET + gameRound.getTimer().buildText(System.currentTimeMillis())))
        }, System.currentTimeMillis());
    }
}
