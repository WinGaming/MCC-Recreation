package mcc.yml.decisiondome;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import mcc.utils.Pair;
import mcc.yml.ConfigUtils;
import mcc.yml.MCCConfigSerializable;

/**
 * This class represents the configuration section for field-type in the
 * Decision-dome implementing {@link MCCConfigSerializable}.
 */
public class HubDecisiondomeFieldTypeConfig implements MCCConfigSerializable {

    private final Material defaultMaterial;

    private Material material;

    public HubDecisiondomeFieldTypeConfig() {
        this.defaultMaterial = Material.ORANGE_WOOL;
        this.material = defaultMaterial;
    }

    public HubDecisiondomeFieldTypeConfig(Material defaultMaterial) {
        this.defaultMaterial = defaultMaterial;
        this.material = defaultMaterial;
    }

    @Override
    public boolean load(ConfigurationSection config) throws IllegalArgumentException {
        Pair<Material, Boolean> value = ConfigUtils.readEnumValue(Material.class, "material", this.defaultMaterial, config);
        this.material = value.getA();
        return value.getB();
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set("material", this.material.name());
    }

    public Material getMaterial() {
        return material;
    }
}