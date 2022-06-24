package me.mastergamercz.bedwars.items;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class Mine extends SpecialItem {


    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.STRING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Mine");
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMinePlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!this.getItem().isSimilar(e.getPlayer().getItemInHand())) return;

        Block block = e.getBlock();

        block.removeMetadata("playerPlaced", Main.getPlugin(Main.class));
        Team minePlacerTeam = PlayerMeta.getMeta(player).getTeam();
        block.setMetadata("mine", new FixedMetadataValue(Main.getPlugin(Main.class), minePlacerTeam.name()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMineBreak(BlockBreakEvent e) {
        Block block = e.getBlock();

        if (!block.hasMetadata("mine"))return;

        e.setCancelled(true);
        block.removeMetadata("mine", Main.getPlugin(Main.class));
        block.setType(Material.AIR);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMineStep(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Block block = player.getLocation().getBlock();

        if (!block.hasMetadata("mine")) return;

        String minePlacer = block.getMetadata("mine").get(0).asString();
        String playerTeam = PlayerMeta.getMeta(player).getTeam().name();
        if(minePlacer.equalsIgnoreCase(playerTeam)) return;

        block.removeMetadata("mine", Main.getPlugin(Main.class));
        block.setType(Material.AIR);
        player.damage(5);

    }


}
