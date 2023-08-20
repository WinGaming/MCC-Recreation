package mcc.commands;

import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcc.MCC;
import mcc.utils.Vector3d;
import mcc.yml.ConfigValidation;
import mcc.yml.decisiondome.FileConfig;
import mcc.yml.decisiondome.HubDecisiondomeConfig;
import mcc.yml.lobby.HubLobbyConfig;

public class MCCCommand implements CommandExecutor {

    private FileConfig<HubDecisiondomeConfig> config;
    private FileConfig<HubLobbyConfig> lobbyConfig;
    private MCC mccInstance;

    public MCCCommand(MCC mcc, FileConfig<HubDecisiondomeConfig> config, FileConfig<HubLobbyConfig> lobbyConfig) {
        this.config = config;
        this.lobbyConfig = lobbyConfig;
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
            sender.sendMessage("/mcc resume");
            sender.sendMessage("/mcc stop");
            sender.sendMessage("/mcc setSpawn");
        } else if (args[0].equalsIgnoreCase("start")) {
            List<String> configErrors = ConfigValidation.validateDecisiondomeConfig(this.config.getConfigInstance());
            if (configErrors.size() != 0) {
                ConfigValidation.sendConfigValidationResult(sender, configErrors);
                return true;
            }

            if (this.mccInstance.getEventInstance() != null) {
                sender.sendMessage("An event is already running, did you mean /mcc resume?");
                sender.sendMessage("To start another event, use /mcc stop to stop the current one");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("/mcc start <eventId>");
            } else {
                sender.sendMessage("Starting event " + args[1]);
                this.mccInstance.startEvent(args[1]);

                sender.sendMessage("The event has started in a paused state to allow everyone to join");
                sender.sendMessage("Use /mcc resume to start the timer for the decisiondome");
                sender.sendMessage("You can use /mcc pause to pause the event, sometimes the pause will");
                sender.sendMessage("only have an effect after a action finished");
            }
        } else if (args[0].equalsIgnoreCase("pause")) {
            sender.sendMessage("Pausing MCC"); // TODO:
        } else if (args[0].equalsIgnoreCase("resume")) {
            if (this.mccInstance.getEventInstance() == null) {
                sender.sendMessage("No event running, did you mean /mcc start?");
            } else if (this.mccInstance.getEventInstance().resume()) {
                sender.sendMessage("Unpaused event");
            } else {
                sender.sendMessage("Could not unpause event, use /mcc pause to pause it");
            }
        } else if (args[0].equalsIgnoreCase("stop")) {
            sender.sendMessage("Stopping MCC"); // TODO:
        } else if (args[0].equalsIgnoreCase("setSpawn")) {
            Location location = ((Player) sender).getLocation();
            this.lobbyConfig.getConfigInstance().setSpawnLocation(new Vector3d(location.getX(), location.getY(), location.getZ()));
            var result = this.lobbyConfig.saveToFile(false);
            sender.sendMessage("Saved lobby config to file: " + result);
        } else {
            sender.sendMessage("Unknown sub-command");
        }

        return true;
    }
}
