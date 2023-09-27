package me.tuskdev.items.command;

import me.saiintbrisson.bukkit.command.command.BukkitContext;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.RarityListManager;
import me.tuskdev.items.util.UltimateFancy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ListCommand {

    private final RarityListManager rarityListManager;
    private final ItemListManager itemListManager;
    private final ConfigurationSection messages;

    public ListCommand(RarityListManager rarityListManager, ItemListManager itemListManager, ConfigurationSection messages) {
        this.rarityListManager = rarityListManager;
        this.itemListManager = itemListManager;
        this.messages = messages;
    }

    @Command(
            name = "customitems.list",
            aliases = { "listar" },
            permission = "customitems.admin",
            target = CommandTarget.PLAYER
    )
    public void handleCommand(Context<Player> context) {
        messages.getStringList("list").forEach(message -> context.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));

        String listItem = ChatColor.translateAlternateColorCodes('&', messages.getString("list-item"));
        itemListManager.getCustomItems().forEach((id, customItem) -> new UltimateFancy(listItem.replace("%item-id%", id).replace("%item-name%", ChatColor.translateAlternateColorCodes('&', customItem.getName())).replace("%item-rarity%", customItem.getRarity() == null ? "Nenhuma" : ChatColor.translateAlternateColorCodes('&', rarityListManager.getRarity(customItem.getRarity())))).clickRunCmd("/ci give " + id).hoverShowText("§aClique para pegar o item.").send(context.getSender()));
    }

}
