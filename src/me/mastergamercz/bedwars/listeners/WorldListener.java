package me.mastergamercz.bedwars.listeners;

import me.mastergamercz.bedwars.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

    private final Main plugin;

    public WorldListener(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e){
        if (e.getEntity().getType().equals(EntityType.VILLAGER) || e.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void weatherChange(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

}
