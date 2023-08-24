package mcc.yml;

import java.io.IOException;
import org.bukkit.configuration.ConfigurationSection;

import mcc.MCC;
import mcc.utils.Pair;

public class ConfigUtils {

	public static <T extends Enum<T>> Pair<T, Boolean> readEnumValue(Class<T> enumClass, String key, T defaultValue, ConfigurationSection config) {
		if (config.contains(key)) {
			return new Pair<>(Enum.valueOf(enumClass, config.getString(key)), false);
		} else {
			return new Pair<>(defaultValue, true);
		}
	}

	public static final <T extends MCCConfigSerializable> FileConfig<T> loadConfig(String configName, T initialInstance) {
		try {
			return new FileConfig<T>(configName, initialInstance);
		} catch (IOException e) {
			System.err.println("Failed to load config-file \"" + configName + "\"! See error message for more details");
			MCC.markStaticLoadError();
			e.printStackTrace();
			return null;
		}
	}
}
