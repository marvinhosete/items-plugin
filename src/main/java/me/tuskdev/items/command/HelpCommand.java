package me.tuskdev.items.command;

import me.saiintbrisson.bukkit.command.command.BukkitContext;
import me.saiintbrisson.minecraft.command.annotation.Command;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class HelpCommand {

    private final ConfigurationSection messages;

    public HelpCommand(ConfigurationSection messages) {
        this.messages = messages;
    }

    @Command(
            name = "customitems.help",
            aliases = { "ajuda" },
            permission = "customitems.admin"
    )
    public void handleCommand(BukkitContext bukkitContext) {
        messages.getStringList("help").forEach(message -> bukkitContext.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

}
