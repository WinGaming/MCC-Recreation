package mcc.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class TeamManager implements Listener {
    
    private List<Team> teams;

    public TeamManager() {
        this.teams = new ArrayList<>();

        // TODO: Load teams from file
        this.teams.add(new Team(new TeamTemplate("Golden Gold", ChatColor.GOLD, '#')));
        this.teams.add(new Team(new TeamTemplate("Ironing Iron", ChatColor.GRAY, '#')));

        this.teams.get(0).addTempNameVar("SiegerSpieler");
        this.teams.get(0).addTempNameVar("Notch");
        this.teams.get(0).addTempNameVar("Grian");
        this.teams.get(0).addTempNameVar("Herobrine");

        this.teams.get(1).addTempNameVar("_Jeb");
        this.teams.get(1).addTempNameVar("Steve");
        this.teams.get(1).addTempNameVar("Alex");
    }

    @EventHandler
    public void onPlayerJoin(PlayerToggleFlightEvent event) {
        // event.getPlayer().setDisplayName("Test");

        // Timer timer = new Timer(TimeUnit.MINUTES, 2);
        // TeamTemplate temaplte = this.teams.get(0).getTemplate();

        // timer.start(System.currentTimeMillis());

        // CachedScoreboardTemplate template = new CachedScoreboardTemplate(IChatBaseComponent.literal(ChatColor.YELLOW + "" + ChatColor.BOLD + "MC Championship Pride 22"), new ScoreboardPartProvider[] {
        //     new SuppliedTimerScoreboardPartProvider(ChatColor.RED + "" + ChatColor.BOLD + "Event begins in:", timer),
        //     (e) -> new Pair<>(new String[] { ChatColor.GREEN + "" + ChatColor.BOLD + "Players: " + ChatColor.RESET +  "0/40"}, 0L),
        //     (e) -> new Pair<>(new String[] {
        //         ChatColor.WHITE + "" + ChatColor.BOLD + "Your Team:",
        //         ChatColor.RESET + "" + temaplte.getIcon() + " " + temaplte.getName() // We need to add reset, because minecraft hides names that start with #
        //     }, 0L),
        //     (e) -> new Pair<>(new String[] {
        //         ChatColor.GREEN + "" + ChatColor.BOLD + "Last Event Coins: " + ChatColor.RESET + "1866~",
        //         ChatColor.GREEN + "" + ChatColor.BOLD + "Lifetime Coins: " + ChatColor.RESET + "28420~"
        //     }, 0L),
        // });
        // template.show(event.getPlayer());

        // Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("MCC-Test"), () -> {
        //     template.show(event.getPlayer());
        // }, 5, 1);

        // TablistDisplay display = new TablistDisplay();
        // ((CraftPlayer) event.getPlayer()).getHandle().connection.send(display.generatePacket(teams, 400));
    }
}
