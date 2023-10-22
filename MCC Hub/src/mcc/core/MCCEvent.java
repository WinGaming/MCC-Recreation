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

    private final ComponentContainer container;

    private EventChapter currentChapter;

    public MCCEvent() {
        this.container = new ComponentContainer();
    }

    /**
     * Updates all elements of the event.
     * @param now The current time in milliseconds
     */
    public void tick(long now) {
        this.container.tick(now);
    }

    /**
     * Switches to the given chapter.
     * @param chapter The chapter to switch to
     */
    public void setCurrentChapter(EventChapter chapter) {
        if (this.currentChapter != null) {
            this.container.clear(); // TODO: Just filter out the components that are not needed anymore
            this.currentChapter.destroy();
        }

        this.currentChapter = chapter;
        this.currentChapter.init();
        this.container.addAll(chapter.createComponents());
    }

    /**
     * Cleans up the event and destroys all components.
     */
    public void destroy() {
        this.container.destroy();
    }

    // TODO: Load components from config
}