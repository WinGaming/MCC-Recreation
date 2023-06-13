package mcc.timer.scripts;

import java.util.ArrayList;
import java.util.List;

import mcc.timer.Timer;

public class Script {
    
    private String name;
    private String component;
    private String state;

    private List<ScriptEvent> events;
    /** A list containing all added events to allow resetting this script */
    private List<ScriptEvent> originalEvents;

    public Script(String name, String component, String state) {
        this.name = name;
        this.component = component;
        this.state = state;
        
        this.events = new ArrayList<>();
    }

    public void addEvent(ScriptEvent event) {
        this.events.add(event);
        this.originalEvents.add(event);
    }

    public void tick(Timer timer, long now) {
        List<ScriptEvent> toRemove = new ArrayList<>();
        for (ScriptEvent event : this.events) {
            if (event.getTrigger().shouldTrigger(timer, now)) {
                event.trigger();
                toRemove.add(event);
            }
        }
        this.events.removeAll(toRemove);
    }

    public void reset() {
        this.events = new ArrayList<>();
        for (ScriptEvent event : this.originalEvents) {
            this.events.add(event);
        }
    }

    public String getName() {
        return name;
    }

    public String getComponent() {
        return component;
    }

    public String getState() {
        return state;
    }
}
