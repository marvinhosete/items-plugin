package me.tuskdev.items.listener;

import me.tuskdev.items.config.ItemListManager;
import me.tuskdev.items.config.data.CustomItem;
import me.tuskdev.items.enums.ItemAttribute;
import me.tuskdev.items.event.ArmorEquipEvent;
import me.tuskdev.items.util.AttributesUtil;
import me.tuskdev.items.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponsAttributesListener implements Listener {

    private final ItemListManager itemListManager;

    public WeaponsAttributesListener(ItemListManager itemListManager) {
        this.itemListManager = itemListManager;
    }

    @EventHandler
    public void onPlayerEquip(ArmorEquipEvent event) {
        Player player = event.getPlayer();

        ItemStack oldWeapon = event.getOldArmorPiece();
        if (oldWeapon != null && oldWeapon.getType() != Material.AIR) {
            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(oldWeapon, "customItem"));
            if (customItem != null) {
                AttributesUtil.removeHealth(player, customItem.getAttribute(ItemAttribute.WEAPONS_LIFE));
                AttributesUtil.removeVelocity(player, customItem.getAttribute(ItemAttribute.WEAPONS_SPEED));
            }
        }

        ItemStack newWeapon = event.getNewArmorPiece();
        if (newWeapon != null && newWeapon.getType() != Material.AIR) {
            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(newWeapon, "customItem"));
            if (customItem != null) {
                AttributesUtil.addHealth(player, customItem.getAttribute(ItemAttribute.WEAPONS_LIFE));
                AttributesUtil.addVelocity(player, customItem.getAttribute(ItemAttribute.WEAPONS_SPEED));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(itemStack, "customItem"));
            if (customItem != null && Math.random() * 100 <= customItem.getAttribute(ItemAttribute.TOOLS_RESISTANCE) * 0.2) {
                event.setDamage(0);
                return;
            }
        }

        event.setDamage(event.getDamage() - total(player, ItemAttribute.WEAPONS_LIFE) * 0.02);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        ItemStack itemStack = player.getItemInHand();

        int total = total(player, ItemAttribute.WEAPONS_DAMAGE);
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(itemStack, "customItem"));
            if (customItem != null) {
                total += customItem.getAttribute(ItemAttribute.ARMORS_DAMAGE);
                if (Math.random() * 100 <= customItem.getAttribute(ItemAttribute.ARMORS_EXTRA_DAMAGE) * 0.2) total *= 2;
            }
        }

        event.setDamage(event.getDamage() + total * 0.04);
    }

    int total(Player player, ItemAttribute itemAttribute) {
        int total = 0;

        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) continue;

            CustomItem customItem = itemListManager.getCustomItem(ItemUtil.getString(itemStack, "customItem"));
            if (customItem == null) continue;

            total += customItem.getAttribute(itemAttribute);
        }

        return total;
    }

}
