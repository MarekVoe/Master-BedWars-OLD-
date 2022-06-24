package me.mastergamercz.bedwars.items;

import me.mastergamercz.bedwars.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class SpecialItem implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    public SpecialItem() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public abstract ItemStack getItem();
}
