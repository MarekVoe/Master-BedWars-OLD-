package me.mastergamercz.bedwars.shop;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.gens.Resource;
import me.mastergamercz.bedwars.maps.GameMap;
import me.mastergamercz.bedwars.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionType;

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
    }),
    ARMOR(Material.IRON_CHESTPLATE, "Armor", (game, player) -> new ShopItem[] {

            new ShopItem(new ItemStack(Material.LEATHER_HELMET, 1) {{
                LeatherArmorMeta meta = (LeatherArmorMeta) this.getItemMeta();
                meta.setColor(PlayerMeta.getMeta(player).getTeam().getColor(PlayerMeta.getMeta(player).getTeam()));
                this.setItemMeta(meta);
            }}, Resource.BRONZE, 1, player),
            new ShopItem(new ItemStack(Material.LEATHER_LEGGINGS, 1) {{
                LeatherArmorMeta meta = (LeatherArmorMeta) this.getItemMeta();
                meta.setColor(PlayerMeta.getMeta(player).getTeam().getColor(PlayerMeta.getMeta(player).getTeam()));
                this.setItemMeta(meta);
            }}, Resource.BRONZE, 1, player),
            new ShopItem(new ItemStack(Material.LEATHER_BOOTS, 1) {{
                LeatherArmorMeta meta = (LeatherArmorMeta) this.getItemMeta();
                meta.setColor(PlayerMeta.getMeta(player).getTeam().getColor(PlayerMeta.getMeta(player).getTeam()));
                this.setItemMeta(meta);
            }}, Resource.BRONZE, 1, player),
            new ShopItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            }}, Resource.IRON, 1, player),
            new ShopItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            }}, Resource.IRON, 3, player),
            new ShopItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
            }}, Resource.IRON, 6, player),
            new ShopItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            }}, Resource.IRON, 9, player)
    }),
    TOOLS(Material.STONE_PICKAXE, "Tools", (game, player) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.SHEARS), Resource.IRON, 2, player),
            new ShopItem(new ItemStack(Material.WOOD_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 1);
            }}, Resource.BRONZE, 4, player),
            new ShopItem(new ItemStack(Material.STONE_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 2);
            }}, Resource.IRON, 2, player),
            new ShopItem(new ItemStack(Material.IRON_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 3);
            }}, Resource.GOLD, 1, player),
            new ShopItem(new ItemStack(Material.DIAMOND_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 3);
            }}, Resource.GOLD, 5, player)
    }),
    MELEE(Material.IRON_SWORD, "Melee", (game, player) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.STICK) {{
                this.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
            }}, Resource.BRONZE, 16, player),
            new ShopItem(new ItemStack(Material.GOLD_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            }}, Resource.IRON, 1, player),
            new ShopItem(new ItemStack(Material.GOLD_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 2);
            }}, Resource.IRON, 3, player),
            new ShopItem(new ItemStack(Material.IRON_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 2);
                this.addEnchantment(Enchantment.KNOCKBACK,2);
            }}, Resource.GOLD, 5, player),
            new ShopItem(new ItemStack(Material.DIAMOND_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 2);
            }}, Resource.GOLD, 10, player),
    }),
    ARCHERY(Material.BOW, "Archery", (game, player) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            }}, Resource.GOLD, 3, player),
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                this.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
            }}, Resource.GOLD, 8, player),
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                this.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
            }}, Resource.GOLD, 12, player),
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                this.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
                this.addEnchantment(Enchantment.ARROW_FIRE, 1);
            }}, Resource.GOLD, 20, player),
            new ShopItem(new ItemStack(Material.ARROW), Resource.GOLD, 1, player)
    }),
    POTIONS(ItemUtils.createPotionItem(PotionType.STRENGTH, 1, false, false, false),
            "Potions", (game, player) -> new ShopItem[]{
            new ShopItem(ItemUtils.createPotionItem(PotionType.INSTANT_HEAL, 2), Resource.IRON, 3, player),
            new ShopItem(ItemUtils.createPotionItem(PotionType.SPEED, 1), Resource.IRON, 6, player),
            new ShopItem(ItemUtils.createPotionItem(PotionType.REGEN, 1), Resource.IRON, 15, player),
            new ShopItem(ItemUtils.createPotionItem(PotionType.STRENGTH, 1), Resource.GOLD, 8, player),
    }),
    UTILITY(Material.TNT, "Utility", (game, player) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.TNT), Resource.GOLD, 2, player),
            new ShopItem(new ItemStack(Material.ENDER_PEARL), Resource.GOLD, 8, player),
            new ShopItem(new ItemStack(Material.WEB), Resource.IRON, 1, player),
            new ShopItem(Main.getPlugin(Main.class).getItemManager().getMine().getItem(), Resource.IRON, 1, player),
            new ShopItem(Main.getPlugin(Main.class).getItemManager().getLuckyBlock().getItem(), Resource.IRON, 5, player)
    });


        private final ItemStack item;
        private final String name;
        private final BiFunction<GameMap, Player, ShopItem[]> items;
        private static Main plugin = Main.getPlugin(Main.class);


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

        private void colorizeItem(ItemStack item, Color color) {
          if (item.getItemMeta() instanceof LeatherArmorMeta) {
              LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
              meta.setColor(color);
              item.setItemMeta(meta);
          }
        }

        public ShopItem[] getItems(GameMap gameMap, Player player) {
          ShopItem[] items = this.items.apply(gameMap, player);
            Arrays.stream(items).forEach(item -> item.addClickAction(event -> new ShopMenu(gameMap, player, this).open()));
            return items;
        }
}
