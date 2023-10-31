package mcc.core.components;

import mcc.display.CachedScoreboardTemplate;
import mcc.yml.event.EventConfig;

public class ComponentPreEventScoreboard extends ComponentScoreboard {

    public ComponentPreEventScoreboard(EventConfig config) {
        super(new CachedScoreboardTemplate(null, "preevent", null)); // TOOD:
        /*new CachedScoreboardTemplate(title, "lobby", MCC.eventConfig.getConfigInstance().getLobbyDisplay().getScoreboardParts(this));*/
    }
}
