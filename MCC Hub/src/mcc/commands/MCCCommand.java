package mcc.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcc.decisiondome.DecisionDomeUtils;
import mcc.yml.hub.HubConfig;

public class MCCCommand implements CommandExecutor {

    private HubConfig config;

    public MCCCommand(HubConfig config) {
        this.config = config;
    }

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
            List<String> configErrors = DecisionDomeUtils.validateConfig(this.config.getDecisiondome());
            if (configErrors.size() != 0) {
                DecisionDomeUtils.sendConfigValidationResult(sender, configErrors);
                return true;
            }

            sender.sendMessage("Starting MCC"); // TODO:
        } else if (args[0].equalsIgnoreCase("stop")) {
            sender.sendMessage("Stopping MCC"); // TODO:
        } else {
            sender.sendMessage("Unknown sub-command");
        }

        return true;
    }
}
