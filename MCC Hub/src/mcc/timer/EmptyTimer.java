package mcc.timer;

import java.util.concurrent.TimeUnit;

public class EmptyTimer extends Timer {

    public EmptyTimer() {
        super(TimeUnit.DAYS, 999);
    }
    
    @Override
    public String buildText(long now) {
        return "00:00";
    }
}
