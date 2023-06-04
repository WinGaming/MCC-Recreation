package mcc.timer.scripts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface ScriptAction {
    
    public static final ScriptAction TITLE = (values) -> {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(values[0], values.length >= 2 ? values[1] : "", 10, 70, 20);
        }
    };

    public static final ScriptAction CHATBOX = (values) -> {
        Bukkit.broadcastMessage("§a§m                                                                                ");

        if (values.length > 8) {
            for (String value : values) {
                Bukkit.broadcastMessage(value);
            }
        } else {
            int topPadding = (int) Math.floor((8 - values.length) / 2);
            for (int i = 0; i < topPadding; i++) {
                Bukkit.broadcastMessage(" ");
            }
            for (String value : values) {
                Bukkit.broadcastMessage(value);
            }
            for (int i = 0; i < 8 - topPadding - values.length; i++) {
                Bukkit.broadcastMessage(" ");
            }
        }

        Bukkit.broadcastMessage("§a§m                                                                                ");
    };

    public static final ScriptAction ERROR = (value) -> {
        System.err.println("Executing unparsed action");
    };

    void execute(String[] values);

}
