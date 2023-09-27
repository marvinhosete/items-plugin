package me.tuskdev.items.command.root;

import com.google.common.collect.ImmutableMap;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.inventory.ViewFrame;
import me.tuskdev.items.util.ItemUtil;
import me.tuskdev.items.view.CustomItemView;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomItemsCommand {

    private final ItemListManager itemListManager;
    private final ViewFrame viewFrame;
    private final ConfigurationSection messages;

    public CustomItemsCommand(ItemListManager itemListManager, ViewFrame viewFrame, ConfigurationSection messages) {
        this.itemListManager = itemListManager;
        this.viewFrame = viewFrame;
        this.messages = messages;
    }

    @Command(
            name = "customitems",
            aliases = { "customitem", "citem", "ci" },
            permission = "customitems.admin",
            target = CommandTarget.PLAYER
    )
    public void handleCommand(Context<Player> context) {
        if (context.argsCount() < 1) {
            context.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("custom-items-usage")));
            return;
        }

        String id = context.getArg(0).toUpperCase();
        CustomItem customItem = itemListManager.getCustomItem(id);

        Player player = context.getSender();
        ItemStack itemStack = player.getItemInHand();

        if ((itemStack == null || itemStack.getType() == Material.AIR) && customItem == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("invalid-item")));
            return;
        }

        if (customItem == null) {
            customItem = new CustomItem(itemStack);
            itemListManager.addCustomItem(id, customItem);
        }

        viewFrame.open(CustomItemView.class, player, ImmutableMap.of("id", id, "item", customItem));
    }

}
