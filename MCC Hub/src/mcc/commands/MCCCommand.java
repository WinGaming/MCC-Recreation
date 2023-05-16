package mcc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MCCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
			sender.sendMessage("Command can only be executed as a player");
			return true;
		}

        if (args.length == 0) {
            sender.sendMessage("/mcc start");
            sender.sendMessage("/mcc stop");
        } else if (args[0].equalsIgnoreCase("start")) {
            sender.sendMessage("Starting MCC"); // TODO:
        } else if (args[0].equalsIgnoreCase("stop")) {
            sender.sendMessage("Stopping MCC"); // TODO:
        } else {
            sender.sendMessage("Unknown sub-command");
        }

        return true;
    }
}
