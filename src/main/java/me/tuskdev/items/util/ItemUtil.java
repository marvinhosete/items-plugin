package me.tuskdev.items.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class ItemUtil {

    private static Class<?> NBTTagCompoundClass;
    private static Method asNMSCopy, asCraftMirror, setNBTTagCompound, hasNBTTagCompound, getNBTTagCompound, setString, getString, hasKey, removeKey;

    static {
        try {
            // Item Classes
            Class<?> itemStackClass = ReflectionUtils.getNMSClass("ItemStack");
            Class<?> craftItemStackClass = ReflectionUtils.getCraftClass("inventory.CraftItemStack");

            // NBTTag Classes
            NBTTagCompoundClass = ReflectionUtils.getNMSClass("NBTTagCompound");

            // Item Handle Methods
            asNMSCopy = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asCraftMirror = craftItemStackClass.getDeclaredMethod("asCraftMirror", itemStackClass);

            // Item NBTTag Methods
            getNBTTagCompound = itemStackClass.getDeclaredMethod("getTag");
            hasNBTTagCompound = itemStackClass.getDeclaredMethod("hasTag");
            setNBTTagCompound = itemStackClass.getDeclaredMethod("setTag", NBTTagCompoundClass);

            // Basic NBTTag Handle Methods
            hasKey = NBTTagCompoundClass.getDeclaredMethod("hasKey", String.class);
            removeKey = NBTTagCompoundClass.getDeclaredMethod("remove", String.class);
            setString = NBTTagCompoundClass.getDeclaredMethod("setString", String.class, String.class);
            getString = NBTTagCompoundClass.getDeclaredMethod("getString", String.class);
        } catch (Exception ignored) {}
    }

    public static boolean hasKey(ItemStack item, String key) {
        try {
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);
            if (hasNBTTag) {
                Object NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
                return (boolean) hasKey.invoke(NBTTagCompound, key);
            }
            return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ItemStack setString(ItemStack item, String key, String value) {
        try	{
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);
            Object NBTTagCompound = hasNBTTag ? getNBTTagCompound.invoke(CraftItemStack) : NBTTagCompoundClass.newInstance();
            setString.invoke(NBTTagCompound, key, value);
            setNBTTagCompound.invoke(CraftItemStack, NBTTagCompound);
            return (ItemStack) asCraftMirror.invoke(null, CraftItemStack);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(ItemStack item, String key) {
        try {
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);
            if (hasNBTTag) {
                Object NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
                return getString.invoke(NBTTagCompound, key).toString();
            }
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack removeKey(ItemStack item, String key) {
        try	{
            Object NBTTagCompound;
            Object CraftItemStack = asNMSCopy.invoke(null, item);
            boolean hasNBTTag = (boolean) hasNBTTagCompound.invoke(CraftItemStack);
            if (hasNBTTag) {
                NBTTagCompound = getNBTTagCompound.invoke(CraftItemStack);
            } else {
                NBTTagCompound = NBTTagCompoundClass.newInstance();
            }
            removeKey.invoke(NBTTagCompound, key);
            setNBTTagCompound.invoke(CraftItemStack, NBTTagCompound);
            return (ItemStack) asCraftMirror.invoke(null, CraftItemStack);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Material tryParseMaterial(String value) {
        try {
            return Material.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.AIR;
        }
    }

}
