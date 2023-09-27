package me.tuskdev.items.listener;

import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.enums.ItemAttribute;
import me.tuskdev.items.util.AttributesUtil;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class ArmorsAttributesListener implements Listener {

    private final ItemListManager itemListManager;

    public ArmorsAttributesListener(ItemListManager itemListManager) {
        this.itemListManager = itemListManager;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        handle(player, player.getInventory().getItem(event.getPreviousSlot()), player.getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        if (event.getSlot() == player.getInventory().getHeldItemSlot())
            handle(player, event.getCurrentItem(), event.getCursor());

        else if (event.getClick().isShiftClick() && event.getSlot() > 8) {
            int emptySlot = -1;
            for (int i = 0; i < 9; i++) {
                if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR) {
                    emptySlot = i;
                    break;
                }
            }

            if (emptySlot == player.getInventory().getHeldItemSlot())
                handle(player, null, event.getCurrentItem());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        handle(event.getPlayer(), event.getItemDrop().getItemStack(), null);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) return;

        int emptySlot = -1;
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR) {
                emptySlot = i;
                break;
            }
        }

        if (emptySlot == player.getInventory().getHeldItemSlot())
            handle(player, null, event.getItem().getItemStack());
    }

    void handle(Player player, ItemStack oldItem, ItemStack newItem) {
        if (oldItem != null && oldItem.getType() != Material.AIR) {
            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(oldItem, "customItem"));
            if (customItem != null) {
                if (customItem.getAttribute(ItemAttribute.ARMORS_SPEED) != 0) player.removePotionEffect(PotionEffectType.SPEED);
                if (customItem.getAttribute(ItemAttribute.ARMORS_RESISTANCE) != 0) player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                if (customItem.getAttribute(ItemAttribute.TOOLS_SPEED) != 0) player.removePotionEffect(PotionEffectType.SPEED);
                if (customItem.getAttribute(ItemAttribute.TOOLS_MINE_SPEED) != 0) player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                if (customItem.getAttribute(ItemAttribute.TOOLS_LIFE) != 0) AttributesUtil.removeHealth(player, customItem.getAttribute(ItemAttribute.TOOLS_LIFE));
            }
        }

        if (newItem != null && newItem.getType() != Material.AIR) {
            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(newItem, "customItem"));
            if (customItem != null) {
                if (customItem.getAttribute(ItemAttribute.ARMORS_SPEED) != 0) player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, (int) (customItem.getAttribute(ItemAttribute.ARMORS_SPEED) * 0.5)));
                if (customItem.getAttribute(ItemAttribute.ARMORS_RESISTANCE) != 0) player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(999999, (int) (customItem.getAttribute(ItemAttribute.ARMORS_RESISTANCE) * 0.5)));
                if (customItem.getAttribute(ItemAttribute.TOOLS_SPEED) != 0) player.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, (int) (customItem.getAttribute(ItemAttribute.ARMORS_SPEED) * 0.5)));
                if (customItem.getAttribute(ItemAttribute.TOOLS_MINE_SPEED) != 0) player.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(999999, (int) (customItem.getAttribute(ItemAttribute.TOOLS_MINE_SPEED) * 0.5)));
                if (customItem.getAttribute(ItemAttribute.TOOLS_LIFE) != 0) AttributesUtil.addHealth(player, customItem.getAttribute(ItemAttribute.TOOLS_LIFE));
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player == null) return;

        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(itemStack, "customItem"));
        if (customItem == null) return;

        int total = 1 + (int) (customItem.getAttribute(ItemAttribute.ARMORS_EXTRA_DROPS) * 0.2);
        if (total == 0) return;

        event.getDrops().forEach(item -> item.setAmount(item.getAmount() * total));
    }

}
