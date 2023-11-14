package mcc.core.team;

import mcc.core.players.EventPlayer;

import java.util.Optional;

public class TeamManager {

    public void resetReadiness() {

    }

    public Optional<Team> getTeamOf(EventPlayer player) {
        return Optional.empty();
    }

    public boolean allTeamsReady() {
        return false; // TODO: This would paused ceitain actions until all teams are ready
    }

    public void tick(long now) {

    }

    public int getPlayerCount() {
        return 0;
    }

    public int getTotalPlayerCount() {
        return 0;
    }
}
