package mcc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.ChatColor;

import mcc.event.PreparedTeam;
import mcc.stats.EventStats;
import mcc.stats.record.EventRecord;
import mcc.stats.record.TeamTemplateRecord;

public class ExampleEventStats implements EventStats {

    @Override
    public Optional<EventRecord> getLastEventBefore(String eventId) {
        return Optional.empty();
    }

    @Override
    public Optional<List<PreparedTeam>> getTeamsForEvent(String eventId) {
        PreparedTeam gold = new PreparedTeam(
            "the_golden_gold",
            Arrays.asList(UUID.fromString("f039b24f-114e-4eb8-964d-50b7c21090d7")),
            new TeamTemplateRecord("Golden Gold", ChatColor.GOLD, '#')
        );

        PreparedTeam iron = new PreparedTeam(
            "the_ironing_iron",
            Arrays.asList(UUID.fromString("61699b2e-d327-4a01-9f1e-0ea8c3f06bc6")),
            new TeamTemplateRecord("Ironing Iron", ChatColor.GRAY, '#')
        );

        return Optional.of(Arrays.asList(gold, iron));
    }
}
