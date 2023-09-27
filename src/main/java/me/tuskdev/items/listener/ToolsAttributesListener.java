package me.tuskdev.items.listener;

import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.enums.ItemAttribute;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ToolsAttributesListener implements Listener {

    private final ItemListManager itemListManager;

    public ToolsAttributesListener(ItemListManager itemListManager) {
        this.itemListManager = itemListManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getType().isBlock()) return;

        CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(itemStack, "customItem"));
        if (customItem == null) return;

        int total = 1 + (int) (customItem.getAttribute(ItemAttribute.TOOLS_EXTRA_DROPS) * 0.2);
        if (total == 0) return;

        Block block = event.getBlock();

        block.getDrops().forEach(drop -> {
            drop.setAmount(drop.getAmount() * total);
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
        });
    }

}
