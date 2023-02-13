package mcc.decisiondome;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcc.MCCTest;
import mcc.config.LocationListSelector;
import mcc.indicator.ParticleIndicator;
import mcc.utils.Pair;

public class DecisionDomeCommand implements CommandExecutor {

	private MCCTest pluginInstance;

	private Map<Player, Pair<DecisionCommandSelectorType, LocationListSelector>> currentSelector;
	
	public DecisionDomeCommand(MCCTest pluginInstance) {
		this.pluginInstance = pluginInstance;
		
		this.currentSelector = new HashMap<>();
	}
	
	public void tick() {
		for (Entry<Player, Pair<DecisionCommandSelectorType, LocationListSelector>> selectorPair : currentSelector.entrySet()) {
			Player player = selectorPair.getKey();
			if (selectorPair.getValue().getA() == DecisionCommandSelectorType.CREATE_FIELD) {
				ParticleIndicator.highlightBlocks(player.getWorld(), selectorPair.getValue().getB().build());
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can only be executed as a player");
			return true;
		}
		
		if (args.length == 0) {
			sender.sendMessage("TODO:");
		} else if (args[0].equalsIgnoreCase("fields")) {
			Player player = (Player) sender;
			
			if (args.length == 1) {
				sender.sendMessage("TODO:");
			} else if (args[1].equalsIgnoreCase("add")) {
				LocationListSelector blockSelector = new LocationListSelector();
				currentSelector.put(player, new Pair<>(DecisionCommandSelectorType.CREATE_FIELD, blockSelector));
				pluginInstance.getConfigBuilder().setSelector(player, blockSelector);
				sender.sendMessage("Started selecting blocks for a field");
			} else if (args[1].equalsIgnoreCase("save")) {
				if (currentSelector.containsKey(player)) {
					switch (currentSelector.get(player).getA()) {
						case CREATE_FIELD: {
							this.pluginInstance.getDecisionDomeTemplate().addField(this.currentSelector.get(player).getB().build());
							sender.sendMessage("Field added to template");
							currentSelector.remove(player);
							pluginInstance.getConfigBuilder().setSelector(player, null);
							break;
						}
					}
				} else {
					sender.sendMessage("You currently have no open selection");
				}
			} else {
				sender.sendMessage("TODO:");
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (this.pluginInstance.getDecisionDome().loadFromTemplate(this.pluginInstance.getDecisionDomeTemplate())) {
				sender.sendMessage("Decision-dome reloaded from template");
			} else {
				sender.sendMessage("Failed to reload decision-dome. This can happen if the decision dome is active");
			}
		} else if (args[0].equalsIgnoreCase("start")) {
			this.pluginInstance.getDecisionDome().start();
		} else {
			sender.sendMessage(String.format(ChatColor.RED + "Unknown sub-command \"%s\"", args[0]));
		}
		
		return true;
	}
	
	public enum DecisionCommandSelectorType {
		CREATE_FIELD
	}
}
