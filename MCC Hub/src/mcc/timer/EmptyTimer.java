package mcc.timer;

import java.util.concurrent.TimeUnit;

public class EmptyTimer extends Timer {

    public EmptyTimer() {
        super(TimeUnit.DAYS, 999);
    }

    public EmptyTimer(TimeUnit unit, int amount) {
        super(unit, amount);
    }
    
    @Override
    public String buildText(long now) {
        return "00:00";
    }
}
