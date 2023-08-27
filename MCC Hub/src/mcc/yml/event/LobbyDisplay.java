package mcc.yml.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import mcc.display.ScoreboardPartProvider;
import mcc.event.Event;
import mcc.utils.Pair;
import mcc.yml.MCCConfigSerializable;
import mcc.yml.event.scoreboardparts.ScoreboardPartsParser;

public class LobbyDisplay implements MCCConfigSerializable {

    private List<Pair<String, ConfigurationSection>> scoreboardParts;

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        boolean valuesChanged = false;

        if (!config.contains("parts")) {
            this.scoreboardParts = new ArrayList<>();
            this.scoreboardParts.add(new Pair<>("eventTimer", new YamlConfiguration()));
            this.scoreboardParts.add(new Pair<>("teamPlayerCount", new YamlConfiguration()));
            this.scoreboardParts.add(new Pair<>("team", new YamlConfiguration()));
            this.scoreboardParts.add(new Pair<>("tempCoins", new YamlConfiguration()));
            valuesChanged = true;
        } else {
            this.scoreboardParts = new ArrayList<>();
            config.getConfigurationSection("parts").getKeys(false).forEach(key -> {
                ConfigurationSection section = config.getConfigurationSection("parts." + key);
                this.scoreboardParts.add(new Pair<>(section.getString("type"), section.getConfigurationSection("config")));
            });
        }

        return valuesChanged;
    }

    @Override
    public void save(ConfigurationSection config) {
        for (int i = 0; i < scoreboardParts.size(); i++) {
            Pair<String, ConfigurationSection> pair = scoreboardParts.get(i);
            config.set("parts." + i + ".type", pair.getA());
            config.set("parts." + i + ".config", pair.getB());
        }
    }

    public ScoreboardPartProvider[] getScoreboardParts(Event event) {
        return scoreboardParts.stream().map(pair -> {
            return ScoreboardPartsParser.parseScoreboardPartProvider(pair.getA(), event, pair.getB());
        }).toArray(ScoreboardPartProvider[]::new);
    }
}
