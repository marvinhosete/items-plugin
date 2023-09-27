package me.tuskdev.items.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArmorType{

    HELMET(39),
    CHESTPLATE(38),
    LEGGINGS(37),
    BOOTS(36);

    private final int slot;

    ArmorType(int slot){
        this.slot = slot;
    }

    public static ArmorType matchType(final ItemStack itemStack){
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;

        String type = itemStack.getType().name();
        if(type.endsWith("_HELMET") || type.endsWith("_SKULL") || type.endsWith("_HEAD")) return HELMET;
        else if(type.endsWith("_CHESTPLATE") || type.equals("ELYTRA")) return CHESTPLATE;
        else if(type.endsWith("_LEGGINGS")) return LEGGINGS;
        else if(type.endsWith("_BOOTS")) return BOOTS;
        else return null;
    }

    public int getSlot(){
        return slot;
    }
}