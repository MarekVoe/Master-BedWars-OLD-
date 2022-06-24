package me.mastergamercz.bedwars.shop;

import me.mastergamercz.bedwars.UI.Menu;
import me.mastergamercz.bedwars.UI.MenuItem;
import me.mastergamercz.bedwars.maps.GameMap;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class ShopMenu extends Menu {
    private final GameMap gameMap;
    private final Category category;

    public ShopMenu(GameMap gameMap, Player player, Category category) {
        super(player, 6);
        this.gameMap = gameMap;
        this.category = category;
    }

    public String getMenuName() {
        return "" + ChatColor.GOLD + ChatColor.BOLD + category.getName();
    }

    protected void setMenuItems() {
        for (int i = 0; i < 9; i++) {
            ItemStack splitter = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());

            if (i < Category.values().length) {
                Category category = Category.values()[i];
                this.setItem(i, new CategoryItem(gameMap, category, player));

                if (this.category.equals(category)) {
                    splitter = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
                }
            }

            ItemMeta splitterMeta = splitter.getItemMeta();
            splitterMeta.setDisplayName(ChatColor.DARK_GRAY + "⬆ Categories");
            splitterMeta.setLore(Collections.singletonList(ChatColor.DARK_GRAY + "⬇ Items"));
            splitter.setItemMeta(splitterMeta);

            this.setItem(i + 9, new MenuItem(splitter));
        }

        int index = 19;
        for (ShopItem item : category.getItems(gameMap, player)) {
            if (index > 52) {
                throw new ArrayIndexOutOfBoundsException("Too many items in shop, category: " + category.getName());
            }

            this.setItem(index, item);

            if ((index - 7) % 9 == 0) {
                index += 3;
            } else {
                index++;
            }
        }
    }

}
