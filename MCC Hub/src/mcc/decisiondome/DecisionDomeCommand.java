package mcc.decisiondome;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mcc.MCCTest;
import mcc.config.LocationListSelector;
import mcc.display.CachedScoreboardTemplate;
import mcc.display.ScoreboardPartProvider;
import mcc.display.ScorelistScoreboardPartProvider;
import mcc.indicator.ParticleIndicator;
import mcc.scores.Scorelist;
import mcc.scores.TimeScore;
import mcc.utils.Pair;
import mcc.yml.HubConfig;
import net.minecraft.network.chat.CommonComponents;

public class DecisionDomeCommand implements CommandExecutor {

	private MCCTest pluginInstance;
	
	private HubConfig config;

	private Map<Player, Pair<DecisionCommandSelectorType, LocationListSelector>> currentSelector;
	
	public DecisionDomeCommand(MCCTest pluginInstance, HubConfig config) {
		this.pluginInstance = pluginInstance;
		
		this.config = config;
		
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

			TimeScore scoreA = new TimeScore(); scoreA.setScore(/* System.currentTimeMillis() + */ 500L);
			TimeScore scoreB = new TimeScore(); scoreB.setScore(/* System.currentTimeMillis() + */ 1300L);
			TimeScore scoreC = new TimeScore(); scoreC.setScore(/* System.currentTimeMillis() + */ 2100L);
			TimeScore scoreD = new TimeScore(); scoreD.setScore(/* System.currentTimeMillis() + */ 4700L);
			TimeScore scoreE = new TimeScore(); scoreE.setScore(/* System.currentTimeMillis() + */ 11500L);

			Scorelist<TimeScore> list = new Scorelist<>();
			list.setScore(UUID.randomUUID(), scoreA);
			list.setScore(UUID.randomUUID(), scoreB);
			list.setScore(UUID.randomUUID(), scoreC);
			list.setScore(UUID.randomUUID(), scoreD);
			list.setScore(Bukkit.getPlayer("SiegerSpieler").getUniqueId(), scoreE);
			CachedScoreboardTemplate template = new CachedScoreboardTemplate(CommonComponents.GUI_ACKNOWLEDGE, new ScoreboardPartProvider[] {
				new ScorelistScoreboardPartProvider<TimeScore, Long>(list),
			});
			template.show((Player) sender);
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
							Optional<String> error = this.config.getDecisiondome().addFieldFromSelector(this.currentSelector.get(player).getB());
							if (!error.isPresent()) {
								sender.sendMessage("Field added to template");
								currentSelector.remove(player);
								pluginInstance.getConfigBuilder().setSelector(player, null);
							} else {
								sender.sendMessage(error.get());
							}
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
			sender.sendMessage("TODO: Remove?");
//			if (this.pluginInstance.getDecisionDome().loadFromTemplate(this.pluginInstance.getDecisionDomeTemplate())) {
//				sender.sendMessage("Decision-dome reloaded from template");
//			} else {
//				sender.sendMessage("Failed to reload decision-dome. This can happen if the decision dome is active");
//			}
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
