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
    }

    private String generateTeamText(int totalLength, int index, int longestIndexWidth) {
        String indexString = index + ".";
        String teamIcon = "#";
        String teamName = "Golden Gold";
        
        String coinString = "1000~";
        int coinStringLength = SpaceFont.getWidthOf(coinString);

        String teamString = indexString + " " + teamIcon + " " + ChatColor.GOLD + teamName + ChatColor.RESET;
        int teamStringLength = SpaceFont.getWidthOf(teamString);

        String nameString = teamString + SpaceFont.getSpaceString(totalLength - teamStringLength - coinStringLength) + coinString;

        int indexWidth = SpaceFont.getWidthOf(indexString + " ");
        String membersString = SpaceFont.getSpaceString(indexWidth) + WidthLimitedString.buildString(totalLength - indexWidth, "SiegerSpieler", "Notch", "Herobrine", "Grian");

        return nameString + "\n" + ChatColor.GOLD + membersString + ChatColor.RESET;
    }

    @EventHandler
    public void onPlayerJoin(PlayerToggleFlightEvent event) {
        // event.getPlayer().setDisplayName("Test");

        int longestIndexWidth = Math.max(SpaceFont.getWidthOf("1."), SpaceFont.getWidthOf("2.")); // TODO:

        String headerTemplate = ChatColor.GOLD + "MCC CHAMPIONSHIP" + ChatColor.RESET;
        headerTemplate += "\n" + ChatColor.YELLOW + "Presented by " + ChatColor.RED + "@Noxcrew" + ChatColor.YELLOW + " & " + ChatColor.AQUA + "@Smajor1995";
        headerTemplate += "\n" + ChatColor.RED + ChatColor.STRIKETHROUGH + SpaceFont.getSpaceString(SpaceFont.getWidthOf('-') * 100);
        headerTemplate += "\n" + ChatColor.WHITE + "Grid Runners - GAME COINS";
        headerTemplate += "\n";
        headerTemplate += "\n" + this.generateTeamText(SpaceFont.getWidthOf("-".repeat(100)), 1, longestIndexWidth);
        headerTemplate += "\n";
        headerTemplate += "\n" + this.generateTeamText(SpaceFont.getWidthOf("-".repeat(100)), 2, longestIndexWidth);

        // ChatModifier test = ChatModifier.EMPTY.withColor(ChatHexColor.parseColor("#FFD700"));
        // ChatModifier test2 = ChatModifier.EMPTY.withBold(true);
        // IChatBaseComponent message = IChatBaseComponent.translatable("test", "Steve").setStyle(test);
        IChatBaseComponent message = IChatBaseComponent.literal(headerTemplate)/*.copy().withStyle(test2) */;
        ChatMessageContent content = new ChatMessageContent("pre", message);

        MessageSigner signer = MessageSigner.create(new UUID(0, 0));
        MessageSignature signature = MessageSignature.EMPTY;
        SignedMessageHeader header = new SignedMessageHeader(signature, event.getPlayer().getUniqueId());
        SignedMessageBody body = new SignedMessageBody(content, signer.timeStamp(), signer.salt(), new LastSeenMessages(Arrays.asList()));

        PlayerChatMessage mesa = new PlayerChatMessage(header, signature, body, Optional.empty(), FilterMask.FULLY_FILTERED);
        // TranslatableComponent test = new TranslatableComponent();
        PacketPlayOutPlayerListHeaderFooter asd = new PacketPlayOutPlayerListHeaderFooter(mesa.serverContent(), mesa.serverContent());
        // ClientboundSystemChatPacket chat = new ClientboundSystemChatPacket(mesa.serverContent(), false);
        ((CraftPlayer) event.getPlayer()).getHandle().connection.send(asd);
    }
}
