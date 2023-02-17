package mcc.yml;

import org.bukkit.configuration.ConfigurationSection;

public interface MCCConfigSerializable {
	
	/**
	 * Loads from the given {@link ConfigurationSection}.
	 * It is recommended to save the configuration if <code>true</code> was returned,
	 * but making sure the load method is not called again
	 * @param config the config to load from
	 * @return <code>true</code> if the configuration needed to be changed, for example adding missing data
	 * @throws IllegalArgumentException when a value was unrecoverable wrong
	 */
	boolean load(ConfigurationSection config) throws IllegalArgumentException;
	
	void save(ConfigurationSection config);
}
