package me.mastergamercz.bedwars.gens;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.enums.GenType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Gold_ItemGenerator extends BukkitRunnable {

    private final Main plugin;
    private GenType genType;
    private double time;
    private final double timeX;
    private Location location;


    public Gold_ItemGenerator(Main plugin, GenType genType, double time, Location location) {
        this.plugin = plugin;
        this.genType = genType;
        this.time = time;
        this.timeX = time;
        this.location = location;

    }

    @Override
    public void run() {
        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemMeta goldMeta = gold.getItemMeta();

        goldMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Gold");
        gold.setItemMeta(goldMeta);

        if (time <= 0) {
            if (genType == GenType.GOLD) {
                if (plugin.hasStarted) {
                    location.getWorld().dropItem(location, gold);
                    time = timeX;
                }
            }
        }
        time = time - 0.05;
    }
}
