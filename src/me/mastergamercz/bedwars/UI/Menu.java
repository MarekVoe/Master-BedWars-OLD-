package me.mastergamercz.bedwars.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class Menu implements InventoryHolder {
    protected final Player player;
    protected final MenuItem[] items;

    public Menu(Player player, int rows) {
        this.player = player;
        this.items = new MenuItem[rows * 9];
    }

    public abstract String getMenuName();

    protected abstract void setMenuItems();

    protected void setItem(int index, MenuItem item) {
        items[index] = item;
    }

    public void handleMenu(InventoryClickEvent event) {
        if (items[event.getSlot()] == null) return;

        MenuItem item = items[event.getSlot()];
        item.doClickActions(event);
    }

    @Override
    public Inventory getInventory() {
        this.setMenuItems();

        Inventory inventory = Bukkit.createInventory(this, this.getSize(), this.getMenuName());

        ItemStack[] itemStacks = Arrays.stream(items)
                .map(item -> item == null ? new ItemStack(Material.AIR) : item.getItemStack())
                .toArray(ItemStack[]::new);

        inventory.setContents(itemStacks);

        return inventory;
    }

    public void open() {
        player.openInventory(this.getInventory());
    }

    public int getSize() {
        return items.length;
    }

    public Player getViewer() {
        return player;
    }
}