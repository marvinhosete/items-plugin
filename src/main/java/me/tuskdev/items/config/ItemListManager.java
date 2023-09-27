package me.tuskdev.items.config;

import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.util.Configuration;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ItemListManager {
    
    private final Map<String, CustomItem> customItems = new HashMap<>();
    private final Configuration configuration;
    
    public ItemListManager(Configuration configuration) {
        ConfigurationSection configurationSection = configuration.getConfigurationSection("items");
        if (configurationSection != null) configurationSection.getKeys(false).forEach(key -> {
            CustomItem customItem = (CustomItem) configuration.get("items." + key);
            customItems.put(key.toUpperCase(), customItem);
        });
        
        this.configuration = configuration;
    }
    
    public CustomItem getCustomItem(String key) {
        if (key == null) return null;

        return customItems.get(key.toUpperCase());
    }
    
    public Map<String, CustomItem> getCustomItems() {
        return customItems;
    }
    
    public void addCustomItem(String key, CustomItem customItem) {
        customItems.put(key.toUpperCase(), customItem);
        configuration.set("items." + key.toUpperCase(), customItem);
        configuration.save();
    }
    
    public void removeCustomItem(String key) {
        customItems.remove(key.toUpperCase());
        configuration.set("items." + key.toUpperCase(), null);
        configuration.save();
    }
    
}
