package mcc.commands;

import mcc.core.MCCEvent;
import mcc.core.event.preevent.ChapterPreEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCCCoreCommand implements CommandExecutor {

    private MCCEvent eventInstance;

    public MCCCoreCommand(MCCEvent eventInstance) {
        this.eventInstance = eventInstance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
			sender.sendMessage("Command can only be executed as a player");
			return true;
		}

        this.eventInstance.setCurrentChapter(new ChapterPreEvent());

        return true;
    }
}
