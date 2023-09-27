package me.tuskdev.items.config.data;

import com.google.common.collect.ImmutableMap;
import me.tuskdev.items.enums.ItemAttribute;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CustomItem implements ConfigurationSerializable {

    private final ItemStack baseItem;
    private String rarity, name;
    private Map<ItemAttribute, Integer> attributes = new HashMap<>();

    public CustomItem(ItemStack baseItem) {
        this.baseItem = baseItem;
    }

    public CustomItem(Map<String, Object> map) {
        this.baseItem = (ItemStack) map.get("baseItem");

        Object rarity = map.get("rarity");
        if (rarity != null) this.rarity = rarity.toString();

        Object name = map.get("name");
        if (name != null) this.name = name.toString();

        Map<String, Integer> attributes = (Map<String, Integer>) map.get("attributes");
        if (attributes != null) attributes.forEach((key, value) -> this.attributes.put(ItemAttribute.valueOf(key), value));
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getName() {
        return name == null ? "Indefinido" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<ItemAttribute, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<ItemAttribute, Integer> attributes) {
        this.attributes = attributes;
    }

    public int getAttribute(ItemAttribute attribute) {
        return attributes.getOrDefault(attribute, 0);
    }

    public void addAttribute(ItemAttribute attribute, int value) {
        attributes.put(attribute, value);
    }

    public void removeAttribute(ItemAttribute attribute) {
        attributes.remove(attribute);
    }

    @Override
    public Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("baseItem", baseItem);
        if (rarity != null) builder.put("rarity", rarity);
        if (name != null) builder.put("name", name);

        Map<String, Integer> attributes = new HashMap<>();
        this.attributes.forEach((key, value) -> attributes.put(key.name(), value));
        builder.put("attributes", attributes);

        return builder.build();
    }
}
