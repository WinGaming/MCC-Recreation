package mcc.core.components.global;

import mcc.core.BukkitConnector;
import mcc.core.Component;
import mcc.core.MCCEvent;
import mcc.core.players.EventPlayer;
import mcc.core.team.Team;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class JoinQuitMessagesComponent implements Component, Listener { // TODO: Remove TEMP_INSTANCE and bukkit dependency

    private final BukkitConnector connector;

    public JoinQuitMessagesComponent(BukkitConnector connector) {
        this.connector = connector;
    }

    @Override
    public void init() {
        this.connector.registerEvents(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        joinEvent.setJoinMessage("");

        EventPlayer eventPlayer = connector.getEventPlayer(joinEvent.getPlayer());
        Optional<Team> team = MCCEvent.getInstance().getTeamManager().getTeamOf(eventPlayer);

        BaseComponent[] chatMessage = new ComponentBuilder()
                .append(team.map(Team::getTeamSymbol).orElse('\uE000') + "")
                .append(eventPlayer.getDisplayName()).color(ChatColor.of(team.map(Team::getTeamHexColor).orElse("#FA3A7F")))
                .append(" joined the game").color(ChatColor.WHITE)
                .create();

        joinEvent.getPlayer().spigot().sendMessage(chatMessage);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent) {
        quitEvent.setQuitMessage("");

        EventPlayer eventPlayer = connector.getEventPlayer(quitEvent.getPlayer());
        Optional<Team> team = MCCEvent.getInstance().getTeamManager().getTeamOf(eventPlayer);

        BaseComponent[] chatMessage = new ComponentBuilder()
                .append(team.map(Team::getTeamSymbol).orElse('\uE000') + "")
                .append(eventPlayer.getDisplayName()).color(ChatColor.of(team.map(Team::getTeamHexColor).orElse("#FA3A7F")))
                .append(" left the game").color(ChatColor.WHITE)
                .create();

        quitEvent.getPlayer().spigot().sendMessage(chatMessage);
    }

    @Override
    public void destroy() {
        PlayerJoinEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
    }
}
