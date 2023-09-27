package me.tuskdev.items.config;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RarityListManager {

    private final Map<String, String> rarities;

    public RarityListManager(ConfigurationSection configurationSection) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        configurationSection.getKeys(false).forEach(key -> builder.put(key, configurationSection.getString(key)));
        rarities = builder.build();
    }

    public String getRarity(String rarity) {
        return rarities.get(rarity);
    }

    public List<String> getRarities() {
        return new ArrayList<>(rarities.values());
    }

    public Map.Entry<String, String> defaultRarity() {
        return rarities.entrySet().stream().findFirst().orElse(null);
    }

}
