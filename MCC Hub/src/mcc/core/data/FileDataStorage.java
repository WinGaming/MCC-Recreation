package mcc.core.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mcc.core.players.EventPlayer;
import mcc.core.players.PlayerStatistics;
import mcc.core.team.Team;
import mcc.core.team.TeamManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileDataStorage {

    private final File dataFolder;

    public FileDataStorage(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public Optional<PlayerStatistics> getPlayerStats(UUID identifier) {
        try {
            File playerFile = new File(this.dataFolder, identifier.toString() + ".json");
            Map<String, Integer> eventCoins = new HashMap<>();
            if (playerFile.exists()) {
                JsonElement element = JsonParser.parseString(FileUtils.readFileToString(playerFile, "UTF-8"));
                JsonObject eventDetails = element.getAsJsonObject().get("events").getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : eventDetails.entrySet()) {
                    eventCoins.put(entry.getKey(), entry.getValue().getAsInt());
                }
            }

            return Optional.of(new PlayerStatistics(eventCoins));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<List<EventPlayer>> loadPlayers(String eventId) {
        List<EventPlayer> players = new ArrayList<>();

        try {
            File eventFile = new File(this.dataFolder, eventId + ".json");
            if (!eventFile.exists()) {
                return Optional.empty();
            }

            JsonObject eventObject = JsonParser.parseString(FileUtils.readFileToString(eventFile, "UTF-8")).getAsJsonObject();
            JsonArray playersArray = eventObject.get("players").getAsJsonArray();

            for (JsonElement element : playersArray) {
                JsonObject playerObject = element.getAsJsonObject();

                // TODO: Check copilot
                UUID identifier = UUID.fromString(playerObject.get("identifier").getAsString());
                String displayName = playerObject.get("displayName").getAsString();

                Optional<PlayerStatistics> stats = this.getPlayerStats(identifier);
                if (!stats.isPresent()) {
                    return Optional.empty();
                }

                players.add(new player);
            }

            return Optional.of(players);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public TeamManager createTeamManager(String eventId) {


        File eventFile = new File(this.dataFolder, eventId + ".json");
        if (!eventFile.exists()) {
            return Optional.empty();
        }

        try {
            JsonObject eventObject = JsonParser.parseString(FileUtils.readFileToString(eventFile, "UTF-8")).getAsJsonObject();
            JsonArray teamsArray = eventObject.get("teams").getAsJsonArray();

            List<Team> teams = new ArrayList<>();
            for (JsonElement element : teamsArray) {
                JsonObject teamObject = element.getAsJsonObject();

                JsonArray playersArray = teamObject.get("players").getAsJsonArray();
                List<EventPlayer> players = new ArrayList<>();
                for (JsonElement playerElement : playersArray) {

                }

                teams.add(new Team(
                        teamObject.get("name").getAsString(),
                        teamObject.get("color").getAsString(),
                        teamObject.get("symbol").getAsString().charAt(0),
                        players
                ));
            }

            return Optional.of(teams);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
