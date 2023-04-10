package mcc.teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import mcc.display.SpaceFont;
import mcc.display.TablistDisplay;
import mcc.utils.LengthLimitedString;
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
    }

    @EventHandler
    public void onPlayerJoin(PlayerToggleFlightEvent event) {
        // event.getPlayer().setDisplayName("Test");

        this.teams.get(0).addTempNameVar("SiegerSpieler");
        this.teams.get(0).addTempNameVar("Notch");
        this.teams.get(0).addTempNameVar("Grian");
        this.teams.get(0).addTempNameVar("Herobrine");

        this.teams.get(1).addTempNameVar("_Jeb");
        this.teams.get(1).addTempNameVar("Steve");
        this.teams.get(1).addTempNameVar("Alex");

        TablistDisplay display = new TablistDisplay();

        ((CraftPlayer) event.getPlayer()).getHandle().connection.send(display.generatePacket(teams, 400));
    }
}
