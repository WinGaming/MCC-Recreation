package mcc.timer.scripts.trigger;

import java.util.concurrent.TimeUnit;

import mcc.timer.Timer;

public class TimeTrigger implements ScriptTrigger {
    
    private TimeUnit unit;
    private int amount;

    public TimeTrigger(TimeUnit unit, int amount) {
        this.unit = unit;
        this.amount = amount;
    }

    @Override
    public boolean shouldTrigger(Timer timer, long now) {
        long remaining = timer.remaining(now);
        return remaining <= this.unit.toMillis(this.amount);
    }
}
