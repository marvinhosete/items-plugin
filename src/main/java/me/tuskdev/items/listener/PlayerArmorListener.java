package me.tuskdev.items.listener;

import java.util.Set;

import me.tuskdev.items.event.ArmorEquipEvent;
import com.google.common.collect.ImmutableSet;
import me.tuskdev.items.enums.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerArmorListener implements Listener {

    private static final Set<Material> blockedMaterials = ImmutableSet.of(Material.FURNACE, Material.CHEST, Material.TRAPPED_CHEST, Material.BEACON, Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.WORKBENCH, Material.ENCHANTMENT_TABLE, Material.ENDER_CHEST, Material.ANVIL, Material.BED_BLOCK, Material.FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.IRON_DOOR_BLOCK, Material.WOODEN_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.WOOD_BUTTON, Material.STONE_BUTTON, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.DARK_OAK_FENCE, Material.ACACIA_FENCE, Material.NETHER_FENCE, Material.BREWING_STAND, Material.CAULDRON, Material.SIGN_POST, Material.WALL_SIGN, Material.SIGN, Material.LEVER, Material.DAYLIGHT_DETECTOR_INVERTED, Material.DAYLIGHT_DETECTOR);

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public final void onInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled() || event.getAction() == InventoryAction.NOTHING) return; // Why does this get called if nothing happens??

        if(
                (event.getSlotType() != SlotType.ARMOR && event.getSlotType() != SlotType.QUICKBAR && event.getSlotType() != SlotType.CONTAINER)
                || (event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.PLAYER)
                || (event.getInventory().getType() != InventoryType.CRAFTING && event.getInventory().getType() != InventoryType.PLAYER)
                || !(event.getWhoClicked() instanceof Player)
        ) return;

        ClickType clickType = event.getClick();
        boolean shift = clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT;
        boolean numberKey = clickType == ClickType.NUMBER_KEY;

        ArmorType armorType = ArmorType.matchType(shift ? event.getCurrentItem() : event.getCursor());
        // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
        if (!shift && armorType != null && event.getSlot() != armorType.getSlot()) return;

        Player player = (Player) event.getWhoClicked();
        if (shift) {
            if (armorType != null) {
                boolean equipping = event.getSlot() != armorType.getSlot();
                if (equipping == isAirOrNull(player.getInventory().getItem(armorType.getSlot()))) {
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.SHIFT_CLICK, armorType, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null);
                    Bukkit.getPluginManager().callEvent(armorEquipEvent);
                }
            }
            return;
        }

        ItemStack newArmorPiece = event.getCursor();
        ItemStack oldArmorPiece = event.getCurrentItem();

        // Prevents shit in the 2by2 crafting
        if (numberKey && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            ItemStack hotBarItem = event.getClickedInventory().getItem(event.getHotbarButton());
            if (isAirOrNull(hotBarItem))
                armorType = ArmorType.matchType(!isAirOrNull(event.getCurrentItem()) ? event.getCurrentItem() : event.getCursor());

            else {
                armorType = ArmorType.matchType(hotBarItem);
                newArmorPiece = hotBarItem;
                oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
            }
        }
        // unequip with no new item going into the slot.
        else if (!numberKey && isAirOrNull(event.getCursor()) && !isAirOrNull(event.getCurrentItem()))
            armorType = ArmorType.matchType(event.getCurrentItem());

        if (armorType != null && event.getSlot() == armorType.getSlot()) {
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(player, event.getAction() == InventoryAction.HOTBAR_SWAP || numberKey ? ArmorEquipEvent.EquipMethod.HOTBAR_SWAP : ArmorEquipEvent.EquipMethod.PICK_DROP, armorType, oldArmorPiece, newArmorPiece);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
        }
    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.useItemInHand().equals(Result.DENY))return;

        if(e.getAction() == Action.PHYSICAL) return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = e.getPlayer();
            if(!e.useInteractedBlock().equals(Result.DENY)){
                if(e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()){// Having both of these checks is useless, might as well do it though.
                    // Some blocks have actions when you right click them which stops the client from equipping the armor in hand.
                    Material mat = e.getClickedBlock().getType();
                    if (blockedMaterials.contains(mat)) return;
                }
            }
            ArmorType newArmorType = ArmorType.matchType(e.getItem());
            if(newArmorType != null){
                if(newArmorType.equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) || newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate()) || newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings()) || newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                }
            }
        }
    }

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(InventoryDragEvent event){
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorType type = ArmorType.matchType(event.getOldCursor());
        if(event.getInventorySlots().isEmpty()) return;// Idk if this will ever happen
        if(type != null && type.getSlot() == event.getInventorySlots().stream().findFirst().orElse(0)){
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
        }
    }

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent e){
        ArmorType type = ArmorType.matchType(e.getBrokenItem());
        if(type != null){
            Player p = e.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(e.getKeepInventory()) return;
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(!isAirOrNull(i)){
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
            }
        }
    }

    public boolean isAirOrNull(final ItemStack itemStack){
        return itemStack == null || itemStack.getType().equals(Material.AIR);
    }
}