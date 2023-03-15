package mcc.teams;

import org.bukkit.ChatColor;

public class TeamTemplate {
    
    private String name;
    private ChatColor color;
    private char icon;

    public TeamTemplate(String name, ChatColor color, char icon) {
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
