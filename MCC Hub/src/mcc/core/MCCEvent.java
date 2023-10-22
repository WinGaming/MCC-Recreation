package mcc.core;

import mcc.core.components.TestComponent;
import mcc.core.event.EventChapter;
import mcc.indicator.ParticleIndicator;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

/**
 * This class represents the current event.
 * It works like a state machine, where the current chapter is the current state.
 */
public class MCCEvent {

    // Accessories, etc. are stored here
    // Basically anything that must be saved between chapters

    private EventChapter currentChapter;
    private ComponentContainer container;

    public MCCEvent() {
        this.container = new ComponentContainer();

        // this.container.addComponent(new TestComponent());
    }

    public void tick(long now) {
        this.container.tick(now);
    }

    public void setCurrentChapter(EventChapter chapter) {
        if (this.currentChapter != null) {
            this.currentChapter.destroy();
        }

        this.currentChapter = chapter;
    }

    public void destroy() {
        this.container.destroy();
    }

    // TODO: Load components from config
    // TODO: Store ComponentContainer somwhow next to the EventChapter instance

}
