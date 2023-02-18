package mcc.yml;

import org.bukkit.configuration.ConfigurationSection;

/**
 * A interface to store and load from a {@link ConfigurationSection} instance.
 */
public interface MCCConfigSerializable {
	
	/**
	 * Loads from the given {@link ConfigurationSection} instance.
	 * It is recommended to save the configuration if <code>true</code> was returned,
	 * but making sure the <code>load</code> method is not called again
	 * @param config the {@link ConfigurationSection} instance to load from
	 * @return <code>true</code> if the configuration needed to be changed (for example adding missing data)
	 * @throws IllegalArgumentException when a value was unrecoverable wrong
	 */
	boolean load(ConfigurationSection config) throws IllegalArgumentException;
	
	/**
	 * Saves the current object into the given {@link ConfigurationSection} instance.
	 * @param config the {@link ConfigurationSection} instance to save into
	 */
	void save(ConfigurationSection config);
}
