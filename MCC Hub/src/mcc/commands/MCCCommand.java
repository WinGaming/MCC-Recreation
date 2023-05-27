package mcc.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcc.MCC;
import mcc.decisiondome.DecisionDomeUtils;
import mcc.yml.hub.HubConfig;

public class MCCCommand implements CommandExecutor {

    private HubConfig config;
    private MCC mccInstance;

    public MCCCommand(MCC mcc, HubConfig config) {
        this.config = config;
        this.mccInstance = mcc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
			sender.sendMessage("Command can only be executed as a player");
			return true;
		}

        if (args.length == 0) {
            sender.sendMessage("/mcc start <eventId>");
            sender.sendMessage("/mcc pause");
            sender.sendMessage("/mcc stop");
        } else if (args[0].equalsIgnoreCase("start")) {
            List<String> configErrors = DecisionDomeUtils.validateConfig(this.config.getDecisiondome());
            if (configErrors.size() != 0) {
                DecisionDomeUtils.sendConfigValidationResult(sender, configErrors);
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("/mcc start <eventId>");
            } else {
                sender.sendMessage("Starting event " + args[1]);
                this.mccInstance.startEvent(args[1]);
            }
        } else if (args[0].equalsIgnoreCase("pause")) {
            sender.sendMessage("Pausing MCC"); // TODO:
        } else if (args[0].equalsIgnoreCase("stop")) {
            sender.sendMessage("Stopping MCC"); // TODO:
        } else {
            sender.sendMessage("Unknown sub-command");
        }

        return true;
    }
}
