package mcc.yml.decisiondome;

import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.ConfigurationSection;

import mcc.yml.MCCConfigSerializable;

public class TimerConfig implements MCCConfigSerializable {

    private TimeUnit timeunit;
    private int amount;
    private long visualDelay;

    public TimerConfig(TimeUnit timeunit, int amount) {
        this(timeunit, amount, 0);
    }

    public TimerConfig(TimeUnit timeunit, int amount, long visualDelay) {
        this.timeunit = timeunit;
        this.amount = amount;
        this.visualDelay = visualDelay;
    }

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        this.timeunit = TimeUnit.valueOf(config.getString("unit"));
        this.amount = config.getInt("amount");
        this.visualDelay = config.getLong("visualDelay");

        return false;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set("unit", this.timeunit.name());
        config.set("amount", this.amount);
        config.set("visualDelay", this.visualDelay);
    }

    public TimeUnit getTimeunit() {
        return timeunit;
    }

    public int getAmount() {
        return amount;
    }

    public long getVisualDelay() {
        return visualDelay;
    }
}
