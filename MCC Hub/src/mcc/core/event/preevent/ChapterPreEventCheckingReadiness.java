package mcc.core.event.preevent;

import mcc.core.BukkitConnector;
import mcc.core.event.EventChapterState;
import mcc.core.MCCEvent;
import mcc.core.players.EventPlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;

import java.util.Optional;

/**
 * This PreEvent-EventChapterState checks the readiness of all teams and starts the countdown to the event.
 */
public class ChapterPreEventCheckingReadiness extends EventChapterState<ChapterPreEventState> {

    private long lastCheck;

    @Override
    public void init() {
        MCCEvent.getInstance().getTeamManager().getTeams().forEach(team -> {
            team.getPlayers().forEach(EventPlayer::resetReady);
        });

        this.lastCheck = System.currentTimeMillis();
    }

    @Override
    public Optional<ChapterPreEventState> tick(long now) {
        if (MCCEvent.getInstance().getTeamManager().allTeamsReady()) {
            return Optional.of(ChapterPreEventState.EVENT_START_COUNTDOWN);
        }

        if (now - this.lastCheck > 15_000) {
            this.lastCheck = now;
            MCCEvent.getInstance().getTeamManager().getTeams().forEach(team -> {
                team.getPlayers().forEach(player -> {
                    if (player.isOnline() && !player.isReady()) {
                        Bukkit.getServer().getPlayer(player.getUniqueId()).spigot().sendMessage(
                                new ComponentBuilder().append("Is everyone on your team ready? /ready").create()
                        );
                    }
                });
            });
        }

        return Optional.empty();
    }
}
