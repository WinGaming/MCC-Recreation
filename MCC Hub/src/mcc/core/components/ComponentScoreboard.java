package mcc.core.components;

import mcc.core.Component;
import mcc.display.CachedScoreboardTemplate;

public class ComponentScoreboard implements Component {

    private CachedScoreboardTemplate template;

    public ComponentScoreboard(CachedScoreboardTemplate template) {
        this.template = template;
    }

    @Override
    public void init() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> this.template.show(player));
    }

    @Override
    public void destroy() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> this.template.hide(player));
    }
}
