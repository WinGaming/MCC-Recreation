package mcc.core.components;

import mcc.core.Component;
import mcc.display.CachedScoreboardTemplate;
import org.bukkit.Bukkit;

public class ComponentScoreboard implements Component {

    private final CachedScoreboardTemplate template;

    public ComponentScoreboard(CachedScoreboardTemplate template) {
        this.template = template;
    }

    @Override
    public void init() {
        Bukkit.getServer().getOnlinePlayers().forEach(this.template::show);
    }

    @Override
    public void destroy() {
        Bukkit.getServer().getOnlinePlayers().forEach(this.template::hide);
    }
}
