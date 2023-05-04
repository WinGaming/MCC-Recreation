package mcc.stats.record;

import org.bukkit.ChatColor;

public class TeamTemplateRecord {
    
    /** The displayed name */
    private String name;
    /** The primary color */
    private ChatColor color;
    /** The icon */
    private char icon;

    public TeamTemplateRecord(String name, ChatColor color, char icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public char getIcon() {
        return icon;
    }
}
