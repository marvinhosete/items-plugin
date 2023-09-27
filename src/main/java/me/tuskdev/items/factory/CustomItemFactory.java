package me.tuskdev.items.factory;

import me.tuskdev.items.config.RarityListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.enums.ItemAttribute;
import me.tuskdev.items.util.ItemBuilder;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CustomItemFactory {

    public static ItemStack build(String id, CustomItem customItem, RarityListManager rarityListManager) {
        String rarityName = rarityListManager.getRarity(customItem.getRarity());
        if (rarityName == null) {
            Map.Entry<String, String> defaultRarity = rarityListManager.defaultRarity();
            customItem.setRarity(defaultRarity.getKey());
            rarityName = defaultRarity.getValue();
        }

        ItemBuilder itemBuilder = ItemBuilder.builder(customItem.getBaseItem()).name(ChatColor.translateAlternateColorCodes('&', customItem.getName()));
        itemBuilder.addLore(0, "§7Raridade: §f" + ChatColor.translateAlternateColorCodes('&', rarityName));
        itemBuilder.addLore(1, "");

        int index = 2;
        for (Map.Entry<ItemAttribute, Integer> entry : customItem.getAttributes().entrySet()) {
            itemBuilder.addLore(index, "§7" + entry.getKey().getDescription() + ": §a" + entry.getValue());
            index++;
        }

        itemBuilder.addLore(index+1, "");

        return id == null ? itemBuilder.build() : ItemUtil.setString(itemBuilder.build(), "customItem", id);
    }

}
