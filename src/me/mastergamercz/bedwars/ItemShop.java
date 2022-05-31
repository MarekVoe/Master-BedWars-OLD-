package me.mastergamercz.bedwars;

import me.mastergamercz.bedwars.enums.DropdownType;
import me.mastergamercz.bedwars.enums.Team;
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

import java.util.ArrayList;
import java.util.List;

public class ItemShop implements Listener {

    private Main plugin;
    private ItemStack bronze;
    private ItemStack iron;
    private ItemStack gold;

    public ItemShop(Main plugin) {
        this.plugin = plugin;
    }


       //Opens shop
       public void openShop(Player player, int size) {
        String title = (ChatColor.GOLD + "" + ChatColor.BOLD + "Shop");
        Inventory inv = Bukkit.createInventory(player,size, title);
        addCategories(player, inv);

        player.openInventory(inv);
    }

    public void init() {
        bronze = new ItemStack(Material.CLAY_BRICK,1);
        iron = new ItemStack(Material.IRON_INGOT, 1);
        gold = new ItemStack(Material.GOLD_INGOT, 1);

        ItemMeta bronzeMeta = bronze.getItemMeta();

        bronzeMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bronze");
        bronze.setItemMeta(bronzeMeta);

        ItemMeta ironMeta = iron.getItemMeta();

        ironMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Iron");
        iron.setItemMeta(ironMeta);

        ItemMeta goldMeta = gold.getItemMeta();

        goldMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gold");
        gold.setItemMeta(goldMeta);
    }


    //Adds categories (shops) to main shop

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


     public void dropDown(Player player, DropdownType type, Inventory inventory) {

        ItemStack wool;
        ItemStack leatherChestplate;
        ItemStack leatherLeggings;
        ItemStack sandstone = new ItemStack(Material.SANDSTONE);
        ItemStack enderStone = new ItemStack(Material.ENDER_STONE);
        ItemStack ironBlock = new ItemStack(Material.IRON_BLOCK);

        List<String> woolLore = new ArrayList<String>();

        switch (PlayerMeta.getMeta(player).getTeam()) {
            case BLUE:
                wool = new ItemStack(Material.WOOL,2, (byte) 11);
                ItemMeta woolMeta = wool.getItemMeta();
                woolMeta.setDisplayName(ChatColor.BLUE + "Wool");
                woolLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "1 Bronze");
                woolMeta.setLore(woolLore);
                wool.setItemMeta(woolMeta);

            break;

            case RED:
                wool = new ItemStack(Material.WOOL, 2, (byte) 14);
                woolMeta = wool.getItemMeta();
                woolMeta.setDisplayName(ChatColor.RED + "Wool");
                woolLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "1 Bronze");
                woolMeta.setLore(woolLore);
                wool.setItemMeta(woolMeta);
            break;

            case GREEN:
                wool = new ItemStack(Material.WOOL,2, (byte) 13);
                woolMeta = wool.getItemMeta();
                woolMeta.setDisplayName(ChatColor.GREEN + "Wool");
                woolLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "1 Bronze");
                woolMeta.setLore(woolLore);
                wool.setItemMeta(woolMeta);
            break;

            case YELLOW:
                wool = new ItemStack(Material.WOOL, 2,(byte) 4);
                woolMeta = wool.getItemMeta();
                woolMeta.setDisplayName(ChatColor.YELLOW + "Wool");
                woolLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "1 Bronze");
                woolMeta.setLore(woolLore);
                wool.setItemMeta(woolMeta);
            break;

            default:
                wool = new ItemStack(Material.WOOL, 2,(byte) 0);
            break;
        }



        switch (type) {
            case BLOCKS:
                inventory.setItem(9, wool);
                inventory.setItem(10, sandstone);
                inventory.setItem(11, enderStone);
                inventory.setItem(12, ironBlock);
            break;

            case ARMOR:
             //TODO
            break;

            case BOWS:

            break;
        }
     }


     // Player reference
     // toBuy - Item that player is buying
     // costItem - Item that player must have in order to buy toBuy item
     // toBuyAmount - Amount of toBuy items
     // costAmount - How much materials that buy costs
     public void buy(Player player, ItemStack toBuy, ItemStack costItem, int costAmount) {

         if (toBuy.getType() == Material.WOOL) {
             if (PlayerMeta.getMeta(player).getTeam() == Team.BLUE) {
                 if (checkMaterials(player, costItem, costAmount)) {
                     player.getInventory().removeItem(bronze);
                     player.getInventory().addItem(new ItemStack(Material.WOOL, 2, (byte) 11));
                 } else {
                     player.closeInventory();
                     player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You dont have enough materials !");
                 }
             } else if (PlayerMeta.getMeta(player).getTeam() == Team.RED) {
                 if (checkMaterials(player, costItem, costAmount)) {
                     player.getInventory().removeItem(bronze);
                     player.getInventory().addItem(new ItemStack(Material.WOOL, 2, (byte) 14));
                 } else {
                     player.closeInventory();
                     player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You dont have enough materials !");
                 }
             } else if (PlayerMeta.getMeta(player).getTeam() == Team.GREEN) {
                 if (checkMaterials(player, costItem, costAmount)) {
                     player.getInventory().removeItem(bronze);
                     player.getInventory().addItem(new ItemStack(Material.WOOL, 2, (byte) 13));
                 } else {
                     player.closeInventory();
                     player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You dont have enough materials !");
                 }
             } else if (PlayerMeta.getMeta(player).getTeam() == Team.YELLOW) {
                 if (checkMaterials(player, costItem, costAmount)) {
                     player.getInventory().removeItem(bronze);
                     player.getInventory().addItem(new ItemStack(Material.WOOL, 2, (byte) 4));
                 } else {
                     player.closeInventory();
                     player.sendMessage(plugin.getPrefix() + ChatColor.RED + "You dont have enough materials !");
                 }
             }



         } else if (toBuy.getType() == Material.SANDSTONE) {
           System.out.println(toBuy.getType().toString());
         }
     }

     public void shiftBuy(Player player, ItemStack toBuy, ItemStack costItem, int costAmount) {
          int amount = 0;

          if (toBuy.getType() == Material.WOOL) {
              if (PlayerMeta.getMeta(player).getTeam() == Team.BLUE) {
                  for (ItemStack item : player.getInventory().getContents()) {
                      if (item == null) continue;
                      switch (item.getType()) {
                          case CLAY_BRICK:
                              amount += item.getAmount();
                              player.getInventory().removeItem(item);
                              bronze.setAmount(amount);
                              player.getInventory().addItem(new ItemStack(Material.WOOL, amount * 2, (byte) 11));
                         break;
                      }
                  }
              } else if (PlayerMeta.getMeta(player).getTeam() == Team.RED) {
                  for (ItemStack item : player.getInventory().getContents()) {
                      if (item == null) continue;
                      switch (item.getType()) {
                          case CLAY_BRICK:
                              amount += item.getAmount();
                              player.getInventory().removeItem(item);
                              bronze.setAmount(amount);
                              player.getInventory().addItem(new ItemStack(Material.WOOL, amount * 2, (byte) 14));
                              break;
                      }
                  }
              } else if (PlayerMeta.getMeta(player).getTeam() == Team.GREEN) {
                  for (ItemStack item : player.getInventory().getContents()) {
                      if (item == null) continue;
                      switch (item.getType()) {
                          case CLAY_BRICK:
                              amount += item.getAmount();
                              player.getInventory().removeItem(item);
                              bronze.setAmount(amount);
                              player.getInventory().addItem(new ItemStack(Material.WOOL, amount * 2, (byte) 13));
                              break;
                      }
                  }
              } else if (PlayerMeta.getMeta(player).getTeam() == Team.YELLOW) {
                  for (ItemStack item : player.getInventory().getContents()) {
                      if (item == null) continue;
                      switch (item.getType()) {
                          case CLAY_BRICK:
                              amount += item.getAmount();
                              player.getInventory().removeItem(item);
                              bronze.setAmount(amount);
                              player.getInventory().addItem(new ItemStack(Material.WOOL, amount * 2, (byte) 4));
                              break;
                      }
                  }
              }
          }

     }

     public boolean checkMaterials(Player player, ItemStack material, int amount) {
         return material.getType() == Material.CLAY_BRICK && player.getInventory().contains(material.getType(), amount);
     }

    public ItemStack getBronze() {
        return bronze;
    }

    public ItemStack getIron() {
        return iron;
    }

    public ItemStack getGold() {
        return gold;
    }
}
