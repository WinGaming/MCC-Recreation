package mcc.yml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import mcc.Constants;

public class HubConfig implements MCCConfigSerializable {
	
	private File configFile;
	private long lastEdited;
	
	private HubDecisiondomeConfig decisiondome;
	
	public HubConfig() throws IOException {
		this.reload();
	}
	
	public void reload() throws IOException {
		File configDir = new File(new File("").getAbsolutePath() + "/" + Constants.FOLDER_CONFIGS);
		File file = new File(configDir.getAbsolutePath() + "/hub.yml");
		
		File parentDir = file.getParentFile();
		if(parentDir != null && !parentDir.exists()) {
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
		this.load(config);
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
		return this.decisiondome.load(config.getConfigurationSection("decisiondome"));
	}
	
	@Override
	public void save(ConfigurationSection config) {
		this.decisiondome.save(config.getConfigurationSection("decisiondome"));
	}
	
	public HubDecisiondomeConfig getDecisiondome() {
		return decisiondome;
	}
	
	public enum FileSaveResponse {
		// No error
		SUCCESS,
		// The file was missing, recreated it from currently known data
		SUCCESS_RECREATED,
		// The file was edited after the last reload, but was overwritten with currently known data
		SUCCESS_FORCED,
		// The file was edited after the last reload
		FAILED_MODIFIED,
		// Unknown error
		FAILED_UNKNOWN,
	}
}
