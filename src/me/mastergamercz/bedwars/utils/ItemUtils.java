package me.mastergamercz.bedwars.utils;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    public static String getItemName(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        return item.getItemMeta().hasDisplayName()
                ? item.getItemMeta().getDisplayName()
                : CraftItemStack.asNMSCopy(item).getName();
    }
}
