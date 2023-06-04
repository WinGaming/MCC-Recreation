package mcc.timer.scripts;

import mcc.timer.scripts.trigger.ScriptTrigger;

public class ScriptEvent {
    
    private ScriptTrigger trigger;
    private ScriptAction action;

    private String[] values;

    public ScriptEvent(ScriptTrigger trigger, ScriptAction action, String[] values) {
        this.trigger = trigger;
        this.action = action;
        this.values = values;
    }

    public ScriptTrigger getTrigger() {
        return trigger;
    }
    
    public void trigger() {
        this.action.execute(this.values);
    }
}
