package mcc.core.event.preevent;

import mcc.core.BukkitConnector;
import mcc.core.Component;
import mcc.core.components.ComponentScoreboard;
import mcc.core.display.PlayerCountScoreboardPartProvider;
import mcc.core.display.PreEventCoinsPartProvider;
import mcc.core.display.TeamScoreboardPartProvider;
import mcc.core.event.EventChapter;
import mcc.core.team.TeamManager;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.StaticScoreboardPartProvider;
import mcc.display.SuppliedTimerScoreboardPartProvider;
import mcc.timer.Timer;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.IChatBaseComponent;
import static org.bukkit.ChatColor.*;

public class ChapterPreEvent extends EventChapter<ChapterPreEventState> {

    public ChapterPreEvent() {
        super(ChapterPreEventState.WAITING);
    }

    @Override
    public Component[] createComponents() {
        return new Component[] {
            new ComponentScoreboard(new CachedScoreboardTemplate(
                    IChatBaseComponent.literal("MC Championship 25").withStyle(EnumChatFormat.BOLD, EnumChatFormat.YELLOW),
                    "preevent",
                    new ScoreboardPartProvider[]{
                            new SuppliedTimerScoreboardPartProvider(this::getCurrentTimerTitle, this::getCurrentTimer),
                            new PlayerCountScoreboardPartProvider(new TeamManager()), // TODO: Use actual TeamManager
                            new TeamScoreboardPartProvider(new TeamManager(), new BukkitConnector()),
                            new PreEventCoinsPartProvider(new BukkitConnector(), "TODO"),
                    }),
                    new BukkitConnector()
            ), // TODO: The bukkitConnector can be removed when loading from config, so no worries here :)
        }; // TODO: Load from config?
    }

    private String getCurrentTimerTitle() {
        return switch (this.getCurrentState()) {
            case EVENT_DECISION_DOME_COUNTDOWN -> RED + "" + BOLD + "Decision Dome in:";
            default -> RED + "" + BOLD + "Event begins in:";
        };
    }

    private Timer getCurrentTimer() {
        return switch (this.getCurrentState()) {
            case EVENT_START_COUNTDOWN -> ((ChapterPreEventEventStartCountdown) this.getCurrentStateInstance()).getTimer();
            case EVENT_DECISION_DOME_COUNTDOWN -> ((ChapterPreEventEventDecisionDomeCountdown) this.getCurrentStateInstance()).getTimer();
            default -> null;
        };
    }
}
