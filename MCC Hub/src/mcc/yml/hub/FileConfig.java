package mcc.yml.hub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import mcc.Constants;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration implementing {@link MCCConfigSerializable}.
 */
public class FileConfig<T extends MCCConfigSerializable> implements MCCConfigSerializable {
	
	private File configFile;
	private long lastEdited;
	
	private String configName;

	private T configInstance;
	
	public FileConfig(String configName, T configInstance) throws IOException {
		this.configName = configName;
		this.configInstance = configInstance;
		
		this.reload();
	}
	
	/**
	 * Loads the configuration from the file-system.
	 * @throws IOException when an unknown io-error occurred
	 */
	public void reload() throws IOException {
		File configDir = new File(new File("").getAbsolutePath() + "/" + Constants.FOLDER_CONFIGS);
		File file = new File(configDir.getAbsolutePath() + "/" + this.configName + ".yml");
		
		File parentDir = file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}
		
		if (!configDir.exists()) { // TODO: Is this even needed?
			configDir.mkdirs();
		}
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		this.configFile = file;
		this.lastEdited = file.lastModified();
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		if (this.load(config)) {
			System.out.println(String.format("Detected patches in \"%s\"-config, force saving changed version...", this.configName));
			this.save(config);
			config.save(file);
		}
	}
	
	/**
	 * Saves the configuration to the file-system.
	 * @param force If <code>true</code> will overwrite the file even if it was modified from another application
	 * @return a {@link FileSaveResponse} representing the actual performed action
	 */
	public FileSaveResponse saveToFile(boolean force) {
		try {
			YamlConfiguration config = new YamlConfiguration();
			this.save(config);
			
			if (!this.configFile.exists()) {
				this.configFile.createNewFile();
				config.save(configFile);
				return FileSaveResponse.SUCCESS_RECREATED;
			}
			
			boolean unchanged = this.lastEdited == this.configFile.lastModified();
			if (unchanged || force) {
				config.save(configFile);

				this.configFile = new File(this.configFile.getAbsolutePath());
				this.lastEdited = this.configFile.lastModified();

				return unchanged ? FileSaveResponse.SUCCESS : FileSaveResponse.SUCCESS_FORCED;
			}
			
			return FileSaveResponse.FAILED_MODIFIED;
		} catch (IOException ex) {
			ex.printStackTrace();
			return FileSaveResponse.FAILED_UNKNOWN;
		}
	}
	
	@Override
	public boolean load(ConfigurationSection config) {
		return this.configInstance.load(config);
	}
	
	@Override
	public void save(ConfigurationSection config) {
		this.configInstance.save(config);
	}
	
	public T getConfigInstance() {
		return configInstance;
	}
	
	public enum FileSaveResponse {
		/** No error */
		SUCCESS,
		/** The file was missing, recreated it from currently known data */
		SUCCESS_RECREATED,
		/** The file was edited after the last reload, but was overwritten with currently known data */
		SUCCESS_FORCED,
		/** The file was edited after the last reload */
		FAILED_MODIFIED,
		/** Unknown error */
		FAILED_UNKNOWN,
	}
}
