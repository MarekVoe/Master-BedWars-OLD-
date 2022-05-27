package me.mastergamercz.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import me.mastergamercz.bedwars.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemShop implements Listener {

    private Main plugin;

    public ItemShop(Main plugin) {
        this.plugin = plugin;
    }

       public void openShop(Player player) {
        int size = 9;
        String title = (ChatColor.GOLD + "" + ChatColor.BOLD + "Shop");
        Inventory inv = Bukkit.createInventory(player,size, title);
        addCategories(player, inv);

        player.openInventory(inv);
    }

    public void addCategories(Player player, Inventory inv) {
        ItemStack blockShop = new ItemStack(Material.SANDSTONE);
        ItemStack armorShop = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) armorShop.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD  + "Armor");
        switch(PlayerMeta.getMeta(player).getTeam()) {
            case RED:
                meta.setColor(Color.RED);
            break;

            case BLUE:
                meta.setColor(Color.BLUE);
            break;

            case GREEN:
                meta.setColor(Color.GREEN);
            break;

            case YELLOW:
                meta.setColor(Color.YELLOW);
            break;
        }
        armorShop.setItemMeta(meta);
        ItemStack toolShop = new ItemStack(Material.STONE_PICKAXE);
        ItemStack weaponShop = new ItemStack(Material.GOLD_SWORD);
        ItemStack bowShop = new ItemStack(Material.BOW);
        ItemStack chestShop = new ItemStack(Material.CHEST);
        ItemStack potionShop = new ItemStack(Material.POTION);
        ItemStack specialShop = new ItemStack(Material.ENDER_PEARL);

        ItemMeta blockMeta = blockShop.getItemMeta();
        blockMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Blocks");

        ItemMeta toolMeta = toolShop.getItemMeta();
        toolMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Tools");

        ItemMeta weaponMeta = weaponShop.getItemMeta();
        weaponMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Weapons");

        ItemMeta bowMeta = bowShop.getItemMeta();
        bowMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bows");

        ItemMeta chestMeta = chestShop.getItemMeta();
        chestMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Chests");

        ItemMeta potionMeta = potionShop.getItemMeta();
        potionMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Potions");

        ItemMeta specialMeta = specialShop.getItemMeta();
        specialMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Specials");

        blockShop.setItemMeta(blockMeta);
        toolShop.setItemMeta(toolMeta);
        weaponShop.setItemMeta(weaponMeta);
        bowShop.setItemMeta(bowMeta);
        chestShop.setItemMeta(chestMeta);
        potionShop.setItemMeta(potionMeta);
        specialShop.setItemMeta(specialMeta);

        inv.addItem(blockShop, armorShop, toolShop, weaponShop, bowShop, chestShop, potionShop, specialShop);
     }
}
