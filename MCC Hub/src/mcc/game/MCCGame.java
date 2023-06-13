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
import mcc.game.map.Map;
import mcc.game.map.MapManager;
import mcc.scores.Score;
import mcc.scores.Scorelist;
import mcc.teams.Team;
import mcc.timer.Timer;
import mcc.utils.Pair;
import mcc.utils.Vector3i;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.IChatBaseComponent;

public abstract class MCCGame<GameState extends Enum<GameState>, T extends Score<T, V>, V> extends Game {
    
    private String title;

    private Event event;

    private Timer timer;

    private Scorelist<T> scorelist;

    private MCCGameState state;
    private GameState gameState;
    private GameState initGameState;

    private CachedScoreboardTemplate cachedScoreboardTemplate;

    private List<List<Team[]>> matches;
    private int currentRound = 0;

    private Map map;
    private List<Vector3i> mapLocations;
    private Location mapCopyAreaStart;

    private Location lobbyLocation;

    public MCCGame(String title, GameState initGameState, Event event, TeamMatcher teamMatcher, Map map, Location lobbyLocation, Location mapCopyareaStart) {
        this.title = title;
        this.event = event;

        this.mapCopyAreaStart = mapCopyareaStart;

        this.lobbyLocation = lobbyLocation;

        long now = System.currentTimeMillis();

        this.state = MCCGameState.STARTING;
        this.timer = new Timer(TimeUnit.SECONDS, 60 + 37);
        this.timer.start(now);

        this.gameState = initGameState;
        this.initGameState = initGameState;
        
        this.matches = teamMatcher.generateMatches(event.getTeamManager());

        this.map = map;

        int biggestRound = 0;
        for (List<Team[]> roundMatches : this.matches) {
            biggestRound = Math.max(biggestRound, roundMatches.size());
        }
        this.mapLocations = MapManager.calculateAreaPositions(this.map.getBlueprint(), biggestRound);

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

    public void updateGameState(GameState gameState) {
        this.gameState = gameState;
        this.timer = getTimer(this.gameState);
        if (this.timer != null) {
            this.timer.start(System.currentTimeMillis());
        }
    }

    public Scorelist<T> getScorelist() {
        return scorelist;
    }

    @Override
    public void teleportPlayers() { // TODO: Use method that allows spreading players out a bit
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(this.lobbyLocation);
        }
    }

    @Override
    public void prepare() {
        for (int i = 0; i < this.mapLocations.size(); i++) {
            Vector3i s = this.mapLocations.get(i);
            this.map.getBlueprint().build(this.mapCopyAreaStart.clone().add(s.getX(), s.getY(), s.getZ()));
        }
    }

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
                    
                    if (this.gameState == null) {
                        this.currentRound++;

                        if (this.currentRound >= this.matches.size()) {
                            this.state = MCCGameState.FINISHED;
                            this.timer = new Timer(TimeUnit.SECONDS, 15); // TODO: what is the correct time?
                        } else {
                            // Resetting areas
                            for (int i = 0; i < this.mapLocations.size(); i++) {
                                Vector3i s = this.mapLocations.get(i);
                                this.resetMapAt(this.mapCopyAreaStart.clone().add(s.getX(), s.getY(), s.getZ()), this.map);
                            }

                            this.updateGameState(this.initGameState);
                        }
                    } else {
                        this.timer = getTimer(this.gameState);
                    }

                    break;
                case FINISHED:
                    for (Player player : Bukkit.getOnlinePlayers()) this.cachedScoreboardTemplate.hide(player);
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

    public GameState getGameState() {
        return gameState;
    }

    private void teleportPlayersIntoMaps() {
        for (int i = 0; i < this.matches.get(this.currentRound).size(); i++) {
        }
    }

    public abstract void resetMapAt(Location mapStart, Map map);

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
