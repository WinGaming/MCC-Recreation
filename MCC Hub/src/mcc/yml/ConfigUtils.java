package mcc.yml;

import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Pair;

public class ConfigUtils {

	public static <T extends Enum<T>> Pair<T, Boolean> readEnumValue(Class<T> enumClass, String key, T defaultValue, ConfigurationSection config) {
		if (config.contains(key)) {
			return new Pair<>(Enum.valueOf(enumClass, config.getString(key)), false);
		} else {
			return new Pair<>(defaultValue, true);
		}
	}
}
