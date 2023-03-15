package mcc.teams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.mojang.authlib.GameProfile;

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
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.World;

public class TeamManager implements Listener {
    
    private List<Team> teams;

    public TeamManager() {
        this.teams = new ArrayList<>();

        // TODO: Load teams from file
        this.teams.add(new Team(new TeamTemplate("Golden Gold", ChatColor.GOLD, '#')));
    }

    @EventHandler
    public void onPlayerJoin(PlayerToggleFlightEvent event) {
        // event.getPlayer().setDisplayName("Test");
        // event.getPlayer().setPlayerListName("ListName");

        ChatModifier test = ChatModifier.EMPTY.withColor(ChatHexColor.parseColor("#FFD700"));
        ChatModifier test2 = ChatModifier.EMPTY.withBold(true);
        IChatBaseComponent message = IChatBaseComponent.translatable("test", "Steve").setStyle(test);
        message = message.copy().append(IChatBaseComponent.literal("literal").copy().withStyle(test2));
        ChatMessageContent content = new ChatMessageContent("pre", message);

        MessageSigner signer = MessageSigner.create(new UUID(0, 0));
        MessageSignature signature = MessageSignature.EMPTY;
        SignedMessageHeader header = new SignedMessageHeader(signature, event.getPlayer().getUniqueId());
        SignedMessageBody body = new SignedMessageBody(content, signer.timeStamp(), signer.salt(), new LastSeenMessages(Arrays.asList()));

        // PlayerChatMessage mesa = new PlayerChatMessage(header, signature, body, Optional.empty(), FilterMask.FULLY_FILTERED);
        // PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) event.getPlayer()).getHandle());
        // ((CraftPlayer) event.getPlayer()).getHandle().connection.send(info);

        GameProfile g2 = new GameProfile(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Steve");
        System.out.println(g2.isComplete());
        System.out.println(g2.getProperties().size());
        g2.getProperties().asMap().forEach((a, b) -> {
            System.out.println(a);
            b.forEach(c -> {
                System.out.println(c.getName());
                System.out.println(c.getValue());
            });
        });
        GameProfile g3 = new GameProfile(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Steven");
        EntityPlayer p2 = new EntityPlayer(MinecraftServer.getServer(), MinecraftServer.getServer().getLevel(World.OVERWORLD), MinecraftServer.ANONYMOUS_PLAYER_PROFILE, null);
        EntityPlayer p3 = new EntityPlayer(MinecraftServer.getServer(), MinecraftServer.getServer().getLevel(World.OVERWORLD), g2, null);
        EntityPlayer p4 = new EntityPlayer(MinecraftServer.getServer(), MinecraftServer.getServer().getLevel(World.OVERWORLD), g3, null);
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, p2);
        ((CraftPlayer) event.getPlayer()).getHandle().connection.send(info);

        PacketPlayOutPlayerInfo info2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, p3);
        ((CraftPlayer) event.getPlayer()).getHandle().connection.send(info2);
        
        PacketPlayOutPlayerInfo info3 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, p4);
        ((CraftPlayer) event.getPlayer()).getHandle().connection.send(info3);

        // // TranslatableComponent test = new TranslatableComponent();
        // PacketPlayOutPlayerListHeaderFooter asd = new PacketPlayOutPlayerListHeaderFooter(mesa.serverContent(), mesa.serverContent());
        // // ClientboundSystemChatPacket chat = new ClientboundSystemChatPacket(mesa.serverContent(), false);
        // ((CraftPlayer) event.getPlayer()).getHandle().connection.send(asd);
    }
}
