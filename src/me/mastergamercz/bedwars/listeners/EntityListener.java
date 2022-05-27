package me.mastergamercz.bedwars.listeners;

import me.mastergamercz.bedwars.Main;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {

    private Main plugin;

    public EntityListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        EntityType entityType = entity.getType();
        if (e.getEntity().getType().equals(EntityType.VILLAGER)) {
          System.out.println(e.getEntity().getLastDamageCause().toString());
        } else {
            System.out.println(e.getEntity().getLastDamageCause().getCause().toString());
        }
    }
}
