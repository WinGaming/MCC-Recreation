package mcc.core.minigame.battlebox;

import mcc.core.ComponentContainer;
import mcc.core.MCCEvent;
import mcc.core.team.Team;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.timer.Timer;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.YELLOW;

public class BattleBoxRound {

    private BattleBoxRoundState state;

    private Map<Team, BattleBoxTeam> teams;

    private Timer timer;
    private CachedScoreboardTemplate scoreboardTemplate;

    private int round = 1;
    private List<BattleBoxDual> battles;

    public BattleBoxRound() {
        this.teams = new HashMap<>();

        this.battles = List.of(
                new BattleBoxDual(
                        MCCEvent.getInstance().getTeamManager().getTeams().get(0),
                        MCCEvent.getInstance().getTeamManager().getTeams().get(1)
                )
        );

        this.scoreboardTemplate = new CachedScoreboardTemplate(
                IChatBaseComponent.literal(YELLOW + "" + BOLD + "MC Championship Pride 22"),
                "battlebox",
                new ScoreboardPartProvider[] {
                    new BattleBoxScoreboard(this),
                }
        );

        for (Team team : MCCEvent.getInstance().getTeamManager().getTeams()) {
            teams.put(team, new BattleBoxTeam(Material.AMETHYST_BLOCK));
        }

        this.state = BattleBoxRoundState.SELECT_KIT;
        this.setupState();
    }

    public void tick(long now) {
        boolean aWon = true, bWon = true;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                Location location = new Location(Bukkit.getWorld("world"), 268 + x, 71, 97 - z);
                Material teamBlockA = teams.get(this.battles.get(0).getTeamA()).getTeamBlock();
                Material teamBlockB = teams.get(this.battles.get(0).getTeamB()).getTeamBlock();
                if (location.getBlock().getType() != teamBlockA) {
                    aWon = false;
                }
                if (location.getBlock().getType() != teamBlockB) {
                    bWon = false;
                }
            }
        }

        if (aWon) {
            Bukkit.broadcastMessage("Team A won");
            return;
        } else if (bWon) {
            Bukkit.broadcastMessage("Team B won");
            return;
        }

        teams.forEach((team, battleBoxTeam) -> {
            team.getPlayers().forEach(player -> {
                if (player.isOnline()) {
                    Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                    this.scoreboardTemplate.show(bukkitPlayer);

                    if (this.state == BattleBoxRoundState.ACTION_COUNTDOWN) {
                        long timeLeft = (this.timer.remaining(now) / 1000) + 1;
                        ChatColor color = ChatColor.WHITE;
                        if (timeLeft == 3) color = ChatColor.RED;
                        else if (timeLeft == 2) color = ChatColor.YELLOW;
                        else if (timeLeft == 1) color = ChatColor.GREEN;
                        bukkitPlayer.sendTitle(ChatColor.AQUA + "Starting in", color + "" + BOLD + ">" + timeLeft + "<", 0, 1, 10);
                    }
                }
            });
        });

        if (this.timer != null && this.timer.remaining(now) <= 0) {
            this.timer = null;
            switch (state) {
                case SELECT_KIT:
                    this.state = BattleBoxRoundState.ACTION_COUNTDOWN;
                    this.setupState();
                    break;
                case ACTION_COUNTDOWN:
                    this.state = BattleBoxRoundState.ACTION;
                    this.setupState();
                    break;
                case ACTION:
                    this.state = BattleBoxRoundState.SUMMERY;
                    this.setupState();
                    break;
                case SUMMERY:
                    Bukkit.broadcastMessage("Round over");
                    this.state = BattleBoxRoundState.SELECT_KIT;
                    this.setupState();
                    break;
            }
        }
    }

    protected Timer getTimer() {
        return this.timer;
    }

    protected String getTimerName() {
        switch (state) {
            case SELECT_KIT:
                return "Kit Selection";
            case ACTION_COUNTDOWN:
                return "Battle starts";
            case ACTION:
                return "Time left";
            case SUMMERY:
                return "idk";
        }
        return "Unknown";
    }

    public void setupState() {
        switch (state) {
            case SELECT_KIT:
                for (BattleBoxDual battle : this.battles) {
                    battle.getTeamA().getPlayers().forEach(player -> {
                        if (player.isOnline()) {
                            Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                            bukkitPlayer.teleport(battle.getKitRoomA());
                            bukkitPlayer.getInventory().setItem(2, new ItemStack(teams.get(battle.getTeamA()).getTeamBlock(), 64));

                            bukkitPlayer.sendTitle("Round " + this.round + ":", battle.getTeamA().buildFullText() + ChatColor.RESET + " vs. " + battle.getTeamB().buildFullText(), 10, 70, 20);
                        }
                    });

                    battle.getTeamB().getPlayers().forEach(player -> {
                        if (player.isOnline()) {
                            Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                            bukkitPlayer.teleport(battle.getKitRoomB());
                            bukkitPlayer.getInventory().setItem(2, new ItemStack(teams.get(battle.getTeamB()).getTeamBlock(), 64));

                            bukkitPlayer.sendTitle("Round " + this.round + ":", battle.getTeamA().buildFullText() + ChatColor.RESET + " vs. " + battle.getTeamB().buildFullText(), 10, 70, 20);
                        }
                    });
                }

                this.timer = new Timer(TimeUnit.SECONDS, 20);
                this.timer.start(System.currentTimeMillis());
                break;
            case ACTION:
                teams.forEach((team, battleBoxTeam) -> {
                    team.getPlayers().forEach(player -> {
                        if (player.isOnline()) {
                            Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                            bukkitPlayer.resetTitle();
                        }
                    });
                });

                this.timer = new Timer(TimeUnit.SECONDS, 10, 1);
                this.timer.start(System.currentTimeMillis());

                break;
            case ACTION_COUNTDOWN:
                for (BattleBoxDual battle : this.battles) {
                    battle.getTeamA().getPlayers().forEach(player -> {
                        if (player.isOnline()) {
                            Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                            bukkitPlayer.teleport(battle.getEntranceA());
                        }
                    });

                    battle.getTeamB().getPlayers().forEach(player -> {
                        if (player.isOnline()) {
                            Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                            bukkitPlayer.teleport(battle.getEntranceB());
                        }
                    });
                }

                this.timer = new Timer(TimeUnit.SECONDS, 5, 1);
                this.timer.start(System.currentTimeMillis());
                break;
            case SUMMERY:
                this.timer = new Timer(TimeUnit.SECONDS, 10);
                this.timer.start(System.currentTimeMillis());
                break;
        }
    }
}
