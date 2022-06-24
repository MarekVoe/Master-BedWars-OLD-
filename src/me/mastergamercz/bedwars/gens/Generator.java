package me.mastergamercz.bedwars.gens;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.enums.GenType;
import me.mastergamercz.bedwars.utils.ItemUtils;
import me.mastergamercz.bedwars.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Generator {
    private final Main plugin;
    private final Location location;
    private final GenType genType;

    private int remainingTime;

    public Generator(Main plugin, Location location, GenType genType) {
        this.plugin = plugin;
        this.location = location;
        this.genType = genType;

        remainingTime = genType.getInterval();

        if (genType.isHologram()) {
            String spawnerName = ItemUtils.getItemName(genType.getResource().getItem());
            spawnStand(2.65).setCustomName(ChatColor.getLastColors(spawnerName) +
                    ChatColor.BOLD + ChatColor.stripColor(spawnerName));

            spawnFloatingItem(1.75f, 0.2);
        }
    }

    public void startGenerator() {
        ArmorStand timeText = genType.isHologram()
                ? spawnStand(2.25)
                : null;

        new BukkitRunnable() {
            @Override
            public void run() {
                remainingTime--;

                if (remainingTime <= 0){
                    ItemUtils.dropItem(location, genType.getResource().getItem());
                    remainingTime = getType().getInterval();
                }

                if (timeText != null) {
                    timeText.setCustomName(ChatColor.WHITE + "Spawns in " + ChatColor.GOLD + remainingTime + " seconds");
                }
            }
        }.runTaskTimer(plugin,0,20);
    }

    private ArmorStand spawnFloatingItem(float rotationsPerCycle, double maxMargin) {
        ArmorStand stand = spawnStand(2);

        stand.setHelmet(new ItemStack(genType.getBlock()));
        stand.setCustomNameVisible(false);

        double marginDivisor = (1 / maxMargin / 2);
        Location originalLocation = stand.getLocation();
        new BukkitRunnable() {
            double modifier = 0;

            @Override
            public void run() {
                Location newLocation = originalLocation.clone();

                double margin = ((modifier % 2 < 1
                        ? modifier % 1
                        : 1 - (modifier % 1)) - 0.5) / marginDivisor;

                newLocation.add(0, margin, 0);
                newLocation.setYaw((float) (Math.sin(modifier) * rotationsPerCycle * 180));

                stand.teleport(newLocation);

                modifier += 0.04;
            }
        }.runTaskTimer(plugin, 1, 1);

        return stand;
    }

    private ArmorStand spawnStand(double yModifier) {
        return Util.spawnStand(location.clone().add(0, yModifier, 0));
    }

    public Location getLocation() {
        return location;
    }

    public GenType getType() {
        return genType;
    }
}

