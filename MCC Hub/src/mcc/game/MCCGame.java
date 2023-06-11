package mcc.game;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.ScorelistScoreboardPartProvider;
import mcc.event.Event;
import mcc.scores.Score;
import mcc.scores.Scorelist;
import mcc.teams.Team;
import mcc.timer.Timer;
import mcc.utils.Pair;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.IChatBaseComponent;

public abstract class MCCGame<GameState extends Enum<GameState>, T extends Score<T, V>, V> extends Game {
    
    private String title;

    private Event event;

    private Timer timer;

    private Scorelist<T> scorelist;

    private MCCGameState state;
    private GameState gameState;

    private CachedScoreboardTemplate cachedScoreboardTemplate;

    private List<List<Team[]>> matches;
    private int currentRound = 0;

    private Map map;

    public MCCGame(String title, GameState initGameState, Event event, TeamMatcher teamMatcher, Map map) {
        this.title = title;
        this.event = event;

        this.map = map;

        long now = System.currentTimeMillis();

        this.state = MCCGameState.STARTING;
        this.timer = new Timer(TimeUnit.SECONDS, 60 + 37);
        this.timer.start(now);

        this.gameState = initGameState;
        
        this.matches = teamMatcher.generateMatches(event.getTeamManager());

        this.scorelist = new Scorelist<>();

        this.cachedScoreboardTemplate = new CachedScoreboardTemplate(IChatBaseComponent.literal("MC ..."), "game", new ScoreboardPartProvider[] {
            (player) -> {
                long nnow = System.currentTimeMillis();

                String timerString = "";
                if (this.state == MCCGameState.STARTING) timerString = "Game begins in:";
                else if (this.state == MCCGameState.INGAME) timerString = this.getTimerText(this.gameState);
                else if (this.state == MCCGameState.FINISHED) timerString = "Back to Hub:";

                return new Pair<>(new String[] {
                    ChatColor.AQUA + "" + ChatColor.BOLD + "Game ?/?: " + ChatColor.RESET + this.title,
                    ChatColor.AQUA + "" + ChatColor.BOLD + "Map: " + ChatColor.RESET + this.map.getName(),
                    ChatColor.GREEN + "" + ChatColor.BOLD + "Round: " + ChatColor.RESET + "" + (this.currentRound + 1) + "/" + this.matches.size(),
                    (ChatColor.RED + "" + ChatColor.BOLD) + (this.timer == null ? timerString : (timerString + " " + ChatColor.RESET + this.timer.buildText(nnow)))
                }, nnow);
            },
            new ScorelistScoreboardPartProvider<>(this.scorelist)
        });
    }

    public Scorelist<T> getScorelist() {
        return scorelist;
    }

    @Override
    public void teleportPlayers() { // TODO: Use method that allows spreading players out a bit
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(this.map.getLobbyLocation());
        }
    }

    @Override
    public void prepare() {}

    @Override
    public boolean tick(long now) {
        if (this.timer != null && this.timer.remaining(now) <= 0) {
            switch (this.state) {
                case STARTING:
                    this.state = MCCGameState.INGAME;
                    this.timer = getTimer(this.gameState);
                    break;
                case INGAME:
                    this.gameState = this.onTimerEnd(this.gameState);
                    this.timer = getTimer(this.gameState);

                    if (this.gameState == null) {
                        this.state = MCCGameState.FINISHED;
                        this.timer = new Timer(TimeUnit.SECONDS, 15); // TODO: what is the correct time?
                    }
                    break;
                case FINISHED:
                    return true;
            }

            if (this.timer != null) {
                this.timer.start(now);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.cachedScoreboardTemplate.show(player);
        }

        return false;
    }

    public abstract String getTimerText(GameState state);

    public abstract Timer getTimer(GameState state);
    public abstract GameState onTimerEnd(GameState state);

    public enum MCCGameState {
        /** The game has not started yet, time for explanations */
        STARTING,
        /** The game is in progress */
        INGAME,
        /** The game has finished, time for some statistics */
        FINISHED
    }
}
