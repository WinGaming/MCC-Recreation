package mcc.commands;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcc.config.AreaSelector;
import mcc.config.ConfigAction;
import mcc.config.ConfigBuilder;
import mcc.config.LocationListSelector;
import mcc.decisiondome.DecisionDomeUtils;
import mcc.yml.hub.HubConfig;

public class DecisionDomeCommand implements CommandExecutor {

	private HubConfig config;
	private ConfigBuilder configBuilder;

	public DecisionDomeCommand(ConfigBuilder configBuilder, HubConfig config) {
		this.config = config;
		this.configBuilder = configBuilder;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can only be executed as a player");
			return true;
		}

		sender.sendMessage(args);
		
		if (args.length == 0) {
			sender.sendMessage("/decisiondome fields [add|save]");
			sender.sendMessage("/decisiondome teambox [add|save]");
			sender.sendMessage("/decisiondome config validate");
		} else if (args[0].equalsIgnoreCase("fields")) {
			Player player = (Player) sender;
			
			if (args.length == 1) {
				sender.sendMessage("/decisiondome fields add");
				sender.sendMessage("/decisiondome fields save");
			} else if (args[1].equalsIgnoreCase("add")) {
				this.configBuilder.setSelector(player, ConfigAction.DECISIONDOME_CREATE_FIELD, new LocationListSelector());
				sender.sendMessage("Started selecting blocks for a field");
			} else if (args[1].equalsIgnoreCase("save")) {
				var currentSelection = this.configBuilder.getCurrentSelection(player);
				if (currentSelection.getA() == ConfigAction.DECISIONDOME_CREATE_FIELD) {
					Optional<String> error = this.config.getDecisiondome().addFieldFromSelector((LocationListSelector) currentSelection.getB());
					if (!error.isPresent()) {
						sender.sendMessage("Field added to template");
						this.configBuilder.cancelSelector(player);
						
						Bukkit.broadcastMessage(this.config.saveToFile(false).name());
					} else {
						sender.sendMessage(error.get());
					}
				} else {
					sender.sendMessage("You currently have no open selection in decisiondome");
				}
			} else {
				sender.sendMessage(String.format(ChatColor.RED + "Unknown \"fields\"-sub-command \"%s\"", args[1]));
			}
		} else if (args[0].equalsIgnoreCase("teambox")) {
			Player player = (Player) sender;

			if (args.length == 1) {
				sender.sendMessage("/decisiondome teambox add");
				sender.sendMessage("/decisiondome teambox save");
			} else if (args[1].equalsIgnoreCase("add")) {
				this.configBuilder.setSelector(player, ConfigAction.DECISIONDOME_CREATE_TEAMBOX, new AreaSelector());
				sender.sendMessage("Started selecting blocks for a teambox");
			} else if (args[1].equalsIgnoreCase("save")) {
				var currentSelection = this.configBuilder.getCurrentSelection(player);
				if (currentSelection.getA() == ConfigAction.DECISIONDOME_CREATE_TEAMBOX) {
					Optional<String> error = this.config.getDecisiondome().addTeamboxFromSelector((AreaSelector) currentSelection.getB());
					if (!error.isPresent()) {
						sender.sendMessage("Teambox added to template");
						this.configBuilder.cancelSelector(player);
						
						Bukkit.broadcastMessage(this.config.saveToFile(false).name());
					} else {
						sender.sendMessage(error.get());
					}
				} else {
					sender.sendMessage("You currently have no open selection in decisiondome");
				}
			} else {
				sender.sendMessage(String.format(ChatColor.RED + "Unknown \"teambox\"-sub-command \"%s\"", args[1]));
			}
		} else if (args[0].equalsIgnoreCase("config")) {
			if (args.length == 1) {
				sender.sendMessage("/decisiondome config validate");
			} else if (args[1].equalsIgnoreCase("validate")) {
				DecisionDomeUtils.sendConfigValidationResult(sender, DecisionDomeUtils.validateConfig(this.config.getDecisiondome()));
			}
		} else {
			sender.sendMessage(String.format(ChatColor.RED + "Unknown sub-command \"%s\"", args[0]));
		}
		
		return true;
	}
}
