package me.tuskdev.items;

import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.tuskdev.items.command.*;
import me.tuskdev.items.command.root.CustomItemsCommand;
import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.RarityListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.conversation.ConversationListener;
import me.tuskdev.items.inventory.ViewFrame;
import me.tuskdev.items.listener.PlayerArmorListener;
import me.tuskdev.items.listener.ArmorsAttributesListener;
import me.tuskdev.items.listener.ToolsAttributesListener;
import me.tuskdev.items.listener.WeaponsAttributesListener;
import me.tuskdev.items.util.Configuration;
import me.tuskdev.items.util.PluginUtil;
import me.tuskdev.items.view.ArmorsAttributesView;
import me.tuskdev.items.view.CustomItemView;
import me.tuskdev.items.view.ToolsAttributesView;
import me.tuskdev.items.view.WeaponsAttributesView;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemsPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        saveDefaultConfig();
        ConfigurationSerialization.registerClass(CustomItem.class);
    }

    @Override
    public void onEnable() {
        Configuration configuration = new Configuration("items", this);

        ItemListManager itemListManager = new ItemListManager(configuration);
        RarityListManager rarityListManager = new RarityListManager(getConfig().getConfigurationSection("rarity"));

        ViewFrame viewFrame = new ViewFrame(this);
        viewFrame.register(
                new ArmorsAttributesView(configuration),
                new CustomItemView(rarityListManager, configuration),
                new ToolsAttributesView(configuration),
                new WeaponsAttributesView(configuration)
        );

        ConfigurationSection configurationSection = getConfig().getConfigurationSection("messages");

        BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(
                new CustomItemsCommand(itemListManager, viewFrame, configurationSection),
                new DeleteCommand(itemListManager, configuration, configurationSection),
                new GiveCommand(itemListManager, rarityListManager, configurationSection),
                new HelpCommand(configurationSection),
                new ListCommand(rarityListManager, itemListManager, configurationSection),
                new ResetCommand(itemListManager, configurationSection)
        );

        PluginUtil.registerListeners(
                this,
                new ArmorsAttributesListener(itemListManager),
                new ConversationListener(),
                new PlayerArmorListener(),
                new ToolsAttributesListener(itemListManager),
                new WeaponsAttributesListener(itemListManager)
        );
    }
}
