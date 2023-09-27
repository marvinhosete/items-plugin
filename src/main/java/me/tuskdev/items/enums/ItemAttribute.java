package me.tuskdev.items.enums;

public enum ItemAttribute {

    TOOLS_MINE_SPEED("Velocidade de mineração"),
    TOOLS_EXTRA_DROPS("Drops extras"),
    TOOLS_LIFE("Vida"),
    TOOLS_RESISTANCE("Resistência"),
    TOOLS_SPEED("Velocidade"),
    ARMORS_DAMAGE("Dano"),
    ARMORS_EXTRA_DROPS("Drops extras"),
    ARMORS_SPEED("Velocidade"),
    ARMORS_RESISTANCE("Resistência"),
    ARMORS_EXTRA_DAMAGE("Dano extra"),
    WEAPONS_RESISTANCE("Resistência"),
    WEAPONS_SPEED("Velocidade"),
    WEAPONS_LIFE("Vida"),
    WEAPONS_DAMAGE("Dano");

    final String description;

    ItemAttribute(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ItemAttribute[] getToolsAttributes() {
        return new ItemAttribute[] {TOOLS_MINE_SPEED, TOOLS_EXTRA_DROPS, TOOLS_LIFE, TOOLS_RESISTANCE, TOOLS_SPEED};
    }

    public static ItemAttribute[] getArmorsAttributes() {
        return new ItemAttribute[] {ARMORS_DAMAGE, ARMORS_EXTRA_DROPS, ARMORS_SPEED, ARMORS_RESISTANCE, ARMORS_EXTRA_DAMAGE};
    }

    public static ItemAttribute[] getWeaponsAttributes() {
        return new ItemAttribute[] {WEAPONS_RESISTANCE, WEAPONS_SPEED, WEAPONS_LIFE, WEAPONS_DAMAGE};
    }

}
