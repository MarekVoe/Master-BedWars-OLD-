package me.mastergamercz.bedwars.shop;

import me.mastergamercz.bedwars.UI.ClickAction;
import me.mastergamercz.bedwars.UI.MenuItem;
import me.mastergamercz.bedwars.gens.Resource;
import me.mastergamercz.bedwars.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;


public class ShopItem extends MenuItem {
    private final ItemStack price;
    private final ItemStack item;
    private final Player player;


    public ShopItem(ItemStack item, ItemStack price, Player player) {
        super(getMenuItem(item, price, player));
        this.price = price;
        this.item = item;
        this.player = player;

        ClickAction buyItem = (event) -> {

            if (!event.isLeftClick()) return;

            if (!canAfford(price, player)) {
                player.sendMessage(ChatColor.RED + "Insufficient amount of " + ItemUtils.getItemName(price) +
                        ChatColor.RED + " to purchase " + ChatColor.WHITE + ItemUtils.getItemName(item));
                return;
            }

            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatColor.RED + "Your inventory is full !");
                return;
            }

            if (event.isShiftClick()) {
                int mulLimit = item.getMaxStackSize() / item.getAmount();
                int resourceAmount = Arrays.stream(player.getInventory().getContents())
                        .filter(price::isSimilar)
                        .mapToInt(ItemStack::getAmount)
                        .sum();

                int mul = Math.min(resourceAmount / price.getAmount(), mulLimit);

                ItemStack newItem = item.clone();
                newItem.setAmount(item.getAmount() * mul);
                ItemStack newPrice = price.clone();
                newPrice.setAmount(price.getAmount() * mul);

                buy(newItem, newPrice, player);
            } else {
                buy(item, price, player);
            }

            player.sendMessage(ChatColor.DARK_GRAY + "Purchased " + ItemUtils.getItemName(item));
        };

        this.addClickAction(buyItem);
    }

    public ShopItem(ItemStack item, Resource priceType, int priceAmount, Player player) {
      this(item, ((Supplier<ItemStack>) () -> {
          ItemStack price = priceType.getItem();
          price.setAmount(priceAmount);
          return price;
      }).get(),player);
    }



    private static ItemStack getMenuItem(ItemStack item, ItemStack price, Player player) {
        String priceName = ItemUtils.getItemName(price);

        ItemStack revampedItem = item.clone();
        ItemMeta itemMeta = revampedItem.getItemMeta();
        List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        itemMeta.setDisplayName((canAfford(price, player) ? ChatColor.GREEN : ChatColor.RED) +
                ChatColor.stripColor(ItemUtils.getItemName(item)));
        itemLore.add(" ");
        itemLore.add(ChatColor.DARK_GRAY + "Cost: " + ChatColor.GOLD + price.getAmount() + " " + priceName);

        itemMeta.setLore(itemLore);
        revampedItem.setItemMeta(itemMeta);
        return revampedItem;


    }

    private static boolean canAfford(ItemStack price, Player player) {
         return player.getInventory().containsAtLeast(price, price.getAmount());
    }

    private static void buy(ItemStack item, ItemStack price, Player player) {
        player.getInventory().removeItem(price);
        player.getInventory().addItem(item);
        player.playSound(player.getLocation(), Sound.CLICK,1, 1);
    }

    public ItemStack getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }
}
