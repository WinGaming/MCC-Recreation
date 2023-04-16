package mcc.teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.SpaceFont;
import mcc.display.TablistDisplay;
import mcc.display.TimerScoreboardPartProvider;
import mcc.utils.LengthLimitedString;
import mcc.utils.Pair;
import mcc.utils.Timer;
import mcc.utils.WidthLimitedString;
import net.minecraft.network.chat.ChatHexColor;
import net.minecraft.network.chat.ChatMessageContent;
import net.minecraft.network.chat.ChatModifier;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MessageSigner;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.chat.SignedMessageHeader;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;

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

        Timer timer = new Timer(TimeUnit.MINUTES, 2);
        TeamTemplate temaplte = this.teams.get(0).getTemplate();

        timer.start(System.currentTimeMillis());

        CachedScoreboardTemplate template = new CachedScoreboardTemplate(IChatBaseComponent.literal(ChatColor.YELLOW + "" + ChatColor.BOLD + "MC Championship Pride 22"), new ScoreboardPartProvider[] {
            new TimerScoreboardPartProvider(ChatColor.RED + "" + ChatColor.BOLD + "Event begins in:", timer),
            (e) -> new Pair<>(new String[] { ChatColor.GREEN + "" + ChatColor.BOLD + "Players: " + ChatColor.RESET +  "0/40"}, 0L),
            (e) -> new Pair<>(new String[] {
                ChatColor.WHITE + "" + ChatColor.BOLD + "Your Team:",
                ChatColor.RESET + "" + temaplte.getIcon() + " " + temaplte.getName() // We need to add reset, because minecraft hides names that start with #
            }, 0L),
            (e) -> new Pair<>(new String[] {
                ChatColor.GREEN + "" + ChatColor.BOLD + "Last Event Coins: " + ChatColor.RESET + "1866~",
                ChatColor.GREEN + "" + ChatColor.BOLD + "Lifetime Coins: " + ChatColor.RESET + "28420~"
            }, 0L),
        });
        template.show(event.getPlayer());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("MCC-Test"), () -> {
            template.show(event.getPlayer());
        }, 5, 1);

        // TablistDisplay display = new TablistDisplay();
        // ((CraftPlayer) event.getPlayer()).getHandle().connection.send(display.generatePacket(teams, 400));
    }
}
