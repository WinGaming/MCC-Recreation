package mcc.game;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.ScorelistScoreboardPartProvider;
import mcc.event.Event;
import mcc.scores.Score;
import mcc.scores.Scorelist;
import mcc.teams.Team;
import mcc.timer.Timer;
import net.minecraft.network.chat.IChatBaseComponent;

public abstract class MCCGame<GameState extends Enum<GameState>, T extends Score<T, V>, V> extends Game {
    
    private String title;

    private Event event;

    private Timer timer;

    private Scorelist<T> scorelist;

    private MCCGameState state;
    private GameState gameState;

    private Location lobbyLocation;

    private CachedScoreboardTemplate cachedScoreboardTemplate;

    private List<List<Team[]>> matches;

    public MCCGame(String title, GameState initGameState, Location lobbyLocation, Event event, TeamMatcher teamMatcher) {
        this.title = title;
        this.event = event;

        this.lobbyLocation = lobbyLocation;

        this.state = MCCGameState.STARTING;
        this.timer = new Timer(TimeUnit.SECONDS, 60 + 37);
        this.timer.start(System.currentTimeMillis());

        this.gameState = initGameState;
        
        this.matches = teamMatcher.generateMatches(event.getTeamManager());

        this.scorelist = new Scorelist<>();

        this.cachedScoreboardTemplate = new CachedScoreboardTemplate(IChatBaseComponent.literal("MC ..."), "game", new ScoreboardPartProvider[] {
            new ScorelistScoreboardPartProvider<>(this.scorelist)
        });
    }

    @Override
    public void teleportPlayers() { // TODO: Use method that allows spreading players out a bit
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(this.lobbyLocation);
        }
    }

    @Override
    public void prepare() {}

    @Override
    public void tick(long now) {
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
                    break;
            }

            if (this.timer != null) {
                this.timer.start(now);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            this.cachedScoreboardTemplate.show(player);
        }
    }

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
