package mcc.core.components;

import mcc.core.BukkitConnector;
import mcc.core.Component;
import mcc.display.CachedScoreboardTemplate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ComponentScoreboard implements Component, Listener {

    private final CachedScoreboardTemplate template;

    public ComponentScoreboard(CachedScoreboardTemplate template, BukkitConnector connector) {
        this.template = template;
        connector.registerEvents(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        this.template.show(joinEvent.getPlayer());
    }

    @Override
    public void onChapterStateChange() {
        Bukkit.getServer().getOnlinePlayers().forEach(this.template::show);
    }

    @Override
    public void init() {
        Bukkit.getServer().getOnlinePlayers().forEach(this.template::show);
    }

    @Override
    public void tick(long now) {
        Bukkit.getServer().getOnlinePlayers().forEach(this.template::show);
    }

    @Override
    public void destroy() {
        Bukkit.getServer().getOnlinePlayers().forEach(this.template::hide);

        PlayerJoinEvent.getHandlerList().unregister(this);
    }
}
