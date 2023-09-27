package me.tuskdev.items.util;

import org.bukkit.entity.Player;

public class AttributesUtil {

    public static void addHealth(Player player, int amount) {
        player.setMaxHealth(player.getMaxHealth() + amount * 0.5);
    }

    public static void addVelocity(Player player, int amount) {
        if (player.getWalkSpeed() >= 1) return;

        float speed = player.getWalkSpeed() + amount * 0.002F;
        if (speed > 1) speed = 1;

        player.setWalkSpeed(speed);
    }

    public static void removeHealth(Player player, int amount) {
        double maxHealth = player.getMaxHealth() - amount * 0.5;
        if (player.getHealth() > maxHealth) player.setMaxHealth(maxHealth);
        player.setMaxHealth(maxHealth);
    }

    public static void removeVelocity(Player player, int amount) {
        if (player.getWalkSpeed() <= 0.2) return;

        float speed = player.getWalkSpeed() - amount * 0.002F;
        if (speed < 0.2) speed = 0.2F;

        player.setWalkSpeed(speed);
    }

}
