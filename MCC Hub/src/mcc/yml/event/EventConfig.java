package mcc.yml.event;

import org.bukkit.configuration.ConfigurationSection;

import mcc.yml.MCCConfigSerializable;

public class EventConfig implements MCCConfigSerializable {

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        boolean valuesChanged = false;

        return valuesChanged;
    }

    @Override
    public void save(ConfigurationSection config) {}
}
