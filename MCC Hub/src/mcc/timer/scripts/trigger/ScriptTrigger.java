package mcc.timer.scripts.trigger;

import mcc.timer.Timer;

@FunctionalInterface
public interface ScriptTrigger {
    
    public static final ScriptTrigger ALWAYS = (timer, now) -> true;
    public static final ScriptTrigger NEVER = (timer, now) -> false;

    boolean shouldTrigger(Timer timer, long now);

}
