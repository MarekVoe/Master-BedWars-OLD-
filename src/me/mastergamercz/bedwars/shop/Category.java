package me.mastergamercz.bedwars.shop;

import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.gens.Resource;
import me.mastergamercz.bedwars.maps.GameMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.BiFunction;


public enum Category {
    BLOCKS(Material.SANDSTONE, "Blocks", (game, player) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.WOOL, 2, PlayerMeta.getMeta(player).getTeam().getDyeColor().getData()),
                    Resource.BRONZE, 1, player),
            new ShopItem(new ItemStack(Material.STAINED_CLAY, 1, PlayerMeta.getMeta(player).getTeam().getDyeColor().getData()),
                    Resource.BRONZE, 2, player),
            new ShopItem(new ItemStack(Material.ENDER_STONE), Resource.BRONZE, 4, player),
            new ShopItem(new ItemStack(Material.IRON_BLOCK), Resource.IRON, 1, player),
            new ShopItem(new ItemStack(Material.OBSIDIAN), Resource.GOLD, 2, player),
    });

        private final ItemStack item;
        private final String name;
        private final BiFunction<GameMap, Player, ShopItem[]> items;

    Category(ItemStack item, String name, BiFunction<GameMap, Player, ShopItem[]> items) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName("" + ChatColor.GOLD +  ChatColor.BOLD + name);
            item.setItemMeta(itemMeta);
            this.item = item;
            this.name = name;
            this.items = items;
    }

    Category(Material material, String name, BiFunction<GameMap, Player, ShopItem[]> items) {
        this(new ItemStack(material), name, items);
    }

        public ItemStack getItem() {
            return item;
        }

        public String getName() {
            return name;
        }

        public ShopItem[] getItems(GameMap gameMap, Player player) {
          ShopItem[] items = this.items.apply(gameMap, player);
            Arrays.stream(items).forEach(item -> item.addClickAction(event -> new ShopMenu(gameMap, player, this).open()));
            return items;
        }
}
