package mcc.yml;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import mcc.MCC;
import mcc.yml.decisiondome.HubDecisiondomeConfig;

public class ConfigValidation {
    
    public static List<String> validateDecisiondomeConfig() {
		List<String> errors = new ArrayList<>();

		HubDecisiondomeConfig config = MCC.decisiondomeConfig.getConfigInstance();
		if (config.getWorldName().equals("WORLD_NAME")) errors.add("world");
		if (config.getFields().length < 2) errors.add("fields.amount");
		if (config.getTeamBoxes().length < 2) errors.add("teamboxes.amount");

		return errors;
	}

	public static void sendConfigValidationResult(CommandSender player, List<String> errors) {
		player.sendMessage("== Found " + errors.size() + " errors ==");
		
		// TODO: Use language files and on hover explanations
		for (String errorKey : errors) {
			if (errorKey.equals("world")) {
				player.sendMessage("Invalid world");
			} else if (errorKey.equals("fields.amount")) {
				player.sendMessage(String.format("Not enough fields"));
				player.sendMessage(String.format("    Use /decisiondome fields add"));
			} else if (errorKey.equals("teamboxes.amount")) {
				player.sendMessage(String.format("Not enough teamboxes"));
				player.sendMessage(String.format("    Use /decisiondome teambox add"));
			} else {
				player.sendMessage(errorKey);
			}
		}

		player.sendMessage("");
	}
}
