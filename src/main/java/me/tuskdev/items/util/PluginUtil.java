package me.tuskdev.items.util;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PluginUtil {

    public static void registerListeners(Plugin plugin, Listener... listeners) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        for (Listener listener : listeners) pluginManager.registerEvents(listener, plugin);
    }

}
