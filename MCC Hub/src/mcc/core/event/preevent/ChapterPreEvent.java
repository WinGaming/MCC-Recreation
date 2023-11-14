package mcc.core.event.preevent;

import mcc.core.BukkitConnector;
import mcc.core.Component;
import mcc.core.components.ComponentScoreboard;
import mcc.core.event.EventChapter;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.StaticScoreboardPartProvider;
import net.minecraft.network.chat.IChatBaseComponent;

public class ChapterPreEvent extends EventChapter<ChapterPreEventState> {

    public ChapterPreEvent() {
        super(ChapterPreEventState.WAITING);
    }

    @Override
    public Component[] createComponents() {
        return new Component[] {
            new ComponentScoreboard(new CachedScoreboardTemplate(IChatBaseComponent.literal("title"), "preevent", new ScoreboardPartProvider[]{
                    new StaticScoreboardPartProvider("Line A", "Line B")
            }), new BukkitConnector()) // TODO: The bukkitConnector can be removed when loading from config, so no worries here :)
        }; // TODO: Load from config?
    }
}
