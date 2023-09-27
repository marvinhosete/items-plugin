package me.tuskdev.items.command;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.RarityListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.factory.CustomItemFactory;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class GiveCommand {

    private final ItemListManager itemListManager;
    private final RarityListManager rarityListManager;
    private final ConfigurationSection messages;

    public GiveCommand(ItemListManager itemListManager, RarityListManager rarityListManager, ConfigurationSection messages) {
        this.itemListManager = itemListManager;
        this.rarityListManager = rarityListManager;
        this.messages = messages;
    }

    @Command(
            name = "customitems.give",
            aliases = { "dar" },
            permission = "customitems.admin",
            target = CommandTarget.PLAYER
    )
    public void handleCommand(Context<Player> context) {
        if (context.argsCount() < 1) {
            context.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("give-usage")));
            return;
        }

        Player target = context.argsCount() >= 2 ? Bukkit.getPlayer(context.getArg(0)) : context.getSender();
        if (target == null) {
            context.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("invalid-player")));
            return;
        }

        String id = context.getArg(context.argsCount() >= 2 ? 1 : 0);
        CustomItem customItem = itemListManager.getCustomItem(id);
        if (customItem == null) {
            context.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("invalid-custom-item")));
            return;
        }

        target.getInventory().addItem(CustomItemFactory.build(id, customItem, rarityListManager));
        context.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("given-item").replace("%player%", target.getName()).replace("%item-id%", id)));
    }

}
