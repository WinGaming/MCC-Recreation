package mcc.core.components.global;

import mcc.core.BukkitConnector;
import mcc.core.Component;
import mcc.core.CoreTest;
import mcc.core.MCCEvent;
import mcc.core.players.EventPlayer;
import mcc.core.team.Team;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class ChatComponent implements Component, Listener { // TODO: Remove TEMP_INSTANCE and bukkit dependency

    private final BukkitConnector connector;

    public ChatComponent(BukkitConnector connector) {
        this.connector = connector;
    }

    @Override
    public void init() {
        Bukkit.getPluginManager().registerEvents(this, CoreTest.TEMP_INSTANCE);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent chatEvent) {
        chatEvent.setCancelled(true);

        EventPlayer eventPlayer = connector.getEventPlayer(chatEvent.getPlayer());
        Optional<Team> team = MCCEvent.getInstance().getTeamManager().getTeamOf(eventPlayer);

        BaseComponent[] chatMessage = new ComponentBuilder()
                .append(team.map(Team::getTeamSymbol).orElse('#') + "")
                .append(eventPlayer.getDisplayName()).color(ChatColor.of(team.map(Team::getTeamHexColor).orElse("#FA3A7F")))
                .append(": ").color(ChatColor.WHITE)
                .append(chatEvent.getMessage())
                .create();

        chatEvent.getPlayer().spigot().sendMessage(chatMessage);
    }

    @Override
    public void destroy() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
    }
}
