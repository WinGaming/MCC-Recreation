package mcc.yml.event;

import org.bukkit.configuration.ConfigurationSection;

import mcc.yml.MCCConfigSerializable;

public class EventConfig implements MCCConfigSerializable {

    private LobbyDisplay lobbyDisplay = new LobbyDisplay();

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        boolean valuesChanged = false;

        if (!config.contains("lobbyDisplay")) config.createSection("lobbyDisplay");
        valuesChanged |= lobbyDisplay.load(config.getConfigurationSection("lobbyDisplay"));

        return valuesChanged;
    }

    @Override
    public void save(ConfigurationSection config) {
        if (!config.contains("lobbyDisplay")) config.createSection("lobbyDisplay");
        lobbyDisplay.save(config.getConfigurationSection("lobbyDisplay"));
    }

    public LobbyDisplay getLobbyDisplay() {
        return lobbyDisplay;
    }
}
