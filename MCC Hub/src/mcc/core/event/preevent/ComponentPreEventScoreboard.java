package mcc.core.event.preevent;

import mcc.core.components.ComponentScoreboard;
import mcc.display.CachedScoreboardTemplate;
import mcc.yml.event.EventConfig;

public class ComponentPreEventScoreboard extends ComponentScoreboard {

    public ComponentPreEventScoreboard(EventConfig config) {
        super(new CachedScoreboardTemplate(null, "preevent", null)); // TOOD:
    }
}
