package me.mastergamercz.bedwars.utils;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.Set;

public class Util {

    public static Location parseLocation(World w, String in) {
        String[] params = in.split(",");
        for (String s : params) {
            s.replace("-0", "0");
        }
        if (params.length == 3 || params.length == 5) {
            double x = Double.parseDouble(params[0]);
            double y = Double.parseDouble(params[1]);
            double z = Double.parseDouble(params[2]);
            Location loc = new Location(w, x, y, z);
            if (params.length == 5) {
                loc.setYaw(Float.parseFloat(params[4]));
                loc.setPitch(Float.parseFloat(params[5]));
            }
            return loc;
        }
        return null;
    }

    public static void sendPlayersToGame(final Player player, Main plugin) {
        final PlayerMeta playerMeta = PlayerMeta.getMeta(player);
        if (playerMeta.getTeam() != null) {
            player.teleport(playerMeta.getTeam().getRandomSpawn());
            player.getInventory().clear();
        }

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run(){
                plugin.getVulnerable().remove(player.getUniqueId());
                player.sendMessage(plugin.getPrefix() + ChatColor.DARK_GRAY + "You are now " + ChatColor.GOLD + "vulnerable");
            }
        }, 3L);
    }


    public static ArmorStand spawnStand(Location location) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setGravity(false);
        stand.setVisible(false);
        stand.setBasePlate(false);
        stand.setCustomNameVisible(true);

        return stand;
    }
}
