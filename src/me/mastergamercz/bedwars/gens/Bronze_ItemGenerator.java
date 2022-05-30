package me.mastergamercz.bedwars.gens;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.enums.GenType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Bronze_ItemGenerator extends BukkitRunnable {

    private final Main plugin;
    private GenType genType;
    private double time;
    private final double timeX;
    private Location location;


    public Bronze_ItemGenerator(Main plugin, GenType genType, double time, Location location) {
        this.plugin = plugin;
        this.genType = genType;
        this.time = time;
        this.timeX = time;
        this.location = location;

    }

    @Override
    public void run() {
        if (time <= 0) {
            if (genType == GenType.BRONZE) {
                if (plugin.hasStarted) {
                    location.getWorld().dropItem(location, new ItemStack(Material.CLAY_BRICK,1));
                    time = timeX;
                }
            }
        }
        time = time - 0.05;
    }
}
