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
import com.mojang.authlib.properties.Property;

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

        // PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) event.getPlayer()).getHandle());
        // ((CraftPlayer) event.getPlayer()).getHandle().connection.send(info);
// 069a79f4-44e9-4726-a5be-fca90e38aaf5
        GameProfile g2 = new GameProfile(UUID.fromString("000a00a0-00a0-0000-a0aa-aaa00a00aaa0"), "Orange");
        String tex = "ewogICJ0aW1lc3RhbXAiIDogMTY4MDkwMTQ3ODA3OSwKICAicHJvZmlsZUlkIiA6ICIyYzA3NmUzNDU0N2M0OWU2OTMwYzQzZDE2MDZmYjI1ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJsUmVzdSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IGZhbHNlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU4MjdmOTUyZDc4MTlkNDVmMmU0Zjg2NTY2OTNlYjgxMzUzZjMzODBmNDhhZjYyMDZkY2YyZGUzNDI1M2YwMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
        String sig = "VXETljTM8oNN2bvSbzIamW4dsVF0oSpyJKBt2jcECMClsedzDj3TUu+FfKzEW9BG6TmZrS+DF9r/9mNbv4/YIxuUQnHX3/ROXWwzcZ3GVyA7FcP6qycF03iwSnAWPlTQvCErR9I1sA/hm5iu0Wi69tfi0mPGaSZnto/5uYWaD5q538sTGZo+JL7jxOgnLQe8dOyKQYcB8vlJP+5XiGuC1Ss0JKSvXl/C0YoZ8G27Q31Z/id7Wo4KyNZ7zUMFIC98Mh5tp8URBK5vcgx3X9eqylPord8uIaMg1ysuoRMlU7+ZRayiUqqT2H63Vdn06cG5xImjiHhrd5Pd6WF2J5SSyo89KcTlslW9fsLE3GI57r4QYbPp0bNoBYjGvra6qnC1b9SE8Fhu3N9apShJqPaqOFLDIJf2US6g32JVxzvP6lGRzKs2uOlN7M4sLypvDKH1qrxpE6YriS4/h8G7Mu3ecqdNHE81g+9mERzqZOVTJdATFGq5rTyb6y0ycKeG/WLzg6Ku8IjvDgntf2925sVUfb7Jckd3OVT51AQJqEcnuyMhpiaCn5mRrvOnbYuK/E6YdXE2iLsK+cazbugFvQN8BsERN9qfA6xPD3zRkSPa5eMVQd51MMVzt7VYBilpPNkzEQY8879laxkjj1KZi/Og6nf5MkAOOI81ymfH3UGxmpE=";
        g2.getProperties().put("textures", new Property("textures", tex, sig));
        Bukkit.broadcastMessage(tex);
        System.out.println("mmmm");
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

        ChatModifier test = ChatModifier.EMPTY.withColor(ChatHexColor.parseColor("#FFD700"));
        ChatModifier test2 = ChatModifier.EMPTY.withBold(true);
        IChatBaseComponent message = IChatBaseComponent.translatable("test", "Steve").setStyle(test);
        message = message.copy().append(IChatBaseComponent.literal("literal").copy().withStyle(test2));
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
