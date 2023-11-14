package mcc.core;

import mcc.commands.MCCCoreCommand;
import mcc.core.components.global.ChatComponent;
import mcc.core.components.global.JoinQuitMessagesComponent;
import mcc.core.event.EventChapter;
import mcc.core.team.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents the current event.
 * It works like a state machine, where the current chapter is the current state.
 */
public class MCCEvent {

    private static MCCEvent INSTANCE;

    public static MCCEvent getInstance() {
        return INSTANCE;
    }

    // Accessories, etc. are stored here
    // Basically anything that must be saved between chapters

    private final TeamManager teamManager;

    private final ComponentContainer globalComponents;
    private final ComponentContainer chapterComponents;

    private EventChapter<?> currentChapter;

    private final BukkitConnector bukkitConnector;

    public MCCEvent(JavaPlugin plugin) {
        if (INSTANCE != null) throw new IllegalStateException("MCCEvent already initialized");

        INSTANCE = this;

        this.bukkitConnector = new BukkitConnector();

        this.teamManager = new TeamManager();

        this.chapterComponents = new ComponentContainer();

        this.globalComponents = new ComponentContainer();
        this.globalComponents.addComponent(new ChatComponent(this.bukkitConnector));
        this.globalComponents.addComponent(new JoinQuitMessagesComponent(this.bukkitConnector));

        plugin.getCommand("mcc").setExecutor(new MCCCoreCommand(this));
    }

    /**
     * Updates all elements of the event.
     * @param now The current time in milliseconds
     */
    public void tick(long now) {
        this.globalComponents.tick(now);
        this.chapterComponents.tick(now);

        if (this.currentChapter != null) {
            this.currentChapter.tick(now);
        }
    }

    /**
     * Switches to the given chapter.
     * @param chapter The chapter to switch to
     */
    public void setCurrentChapter(EventChapter<?> chapter) {
        if (this.currentChapter != null) {
            this.chapterComponents.clear(); // TODO: Just filter out the components that are not needed anymore
            this.currentChapter.destroy();
        }

        this.currentChapter = chapter;
        this.currentChapter.init();
        this.chapterComponents.addAll(chapter.createComponents());
    }

    /**
     * Cleans up the event and destroys all components.
     */
    public void destroy() {
        this.globalComponents.destroy();
        this.chapterComponents.destroy();
    }

    // TODO: Load components from config

    /**
     * Returns the team manager.
     * @return The team manager
     */
    public TeamManager getTeamManager() {
        return teamManager;
    }
}
