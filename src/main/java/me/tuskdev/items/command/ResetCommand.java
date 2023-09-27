package me.tuskdev.items.command;

import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ResetCommand {

    private final ItemListManager itemListManager;
    private final ConfigurationSection messages;

    public ResetCommand(ItemListManager itemListManager, ConfigurationSection messages) {
        this.itemListManager = itemListManager;
        this.messages = messages;
    }

    @Command(
            name = "customitems.reset",
            aliases = { "resetar" },
            permission = "customitems.admin",
            target = CommandTarget.PLAYER
    )
    public void handleCommand(Context<Player> context) {
        Player player = context.getSender();
        ItemStack itemStack = player.getItemInHand();

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("invalid-item")));
            return;
        }

        String id = ItemUtil.getString(itemStack, "customItem");
        if (id == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("not-custom-item")));
            return;
        }

        CustomItem customItem = itemListManager.getCustomItem(id);
        if (customItem == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("not-custom-item")));
            return;
        }

        player.setItemInHand(ItemUtil.removeKey(itemStack, "customItem"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("reseted-item")));
    }

}
