package me.tuskdev.items.command;

import me.saiintbrisson.bukkit.command.command.BukkitContext;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.util.Configuration;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class DeleteCommand {

    private final ItemListManager itemListManager;
    private final Configuration itemsConfiguration;
    private final ConfigurationSection messages;

    public DeleteCommand(ItemListManager itemListManager, Configuration itemsConfiguration, ConfigurationSection messages) {
        this.itemListManager = itemListManager;
        this.itemsConfiguration = itemsConfiguration;
        this.messages = messages;
    }

    @Command(
            name = "customitems.delete",
            aliases = { "deletar" },
            permission = "customitems.admin"
    )
    public void handleCommand(BukkitContext bukkitContext) {
        if (bukkitContext.getArgs().length < 1) {
            bukkitContext.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("delete-usage")));
            return;
        }

        String id = bukkitContext.getArg(0);
        CustomItem customItem = itemListManager.getCustomItem(id);
        if (customItem == null) {
            bukkitContext.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("invalid-custom-item")));
            return;
        }

        itemListManager.removeCustomItem(id);
        bukkitContext.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("custom-item-deleted")));
    }

}
