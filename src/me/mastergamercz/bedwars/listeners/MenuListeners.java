package me.mastergamercz.bedwars.listeners;

import me.mastergamercz.bedwars.UI.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuListeners implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
        public void onMenuClick(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player)) return;

            Inventory inv = e.getView().getTopInventory();
            ItemStack item = e.getCurrentItem();

            if (inv == null || item == null) return;

            InventoryHolder holder = e.getInventory().getHolder();
            if (!(holder instanceof Menu)) return;

            e.setCancelled(true);

            if(e.getClickedInventory() != inv) return;

            Menu menu = (Menu) holder;
            menu.handleMenu(e);
        }
    }

