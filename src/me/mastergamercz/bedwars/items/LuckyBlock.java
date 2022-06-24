package me.mastergamercz.bedwars.items;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.shop.Category;
import me.mastergamercz.bedwars.shop.ShopItem;
import me.mastergamercz.bedwars.utils.ItemUtils;
import me.mastergamercz.bedwars.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LuckyBlock extends SpecialItem {

    private Main plugin = Main.getPlugin(Main.class);

    public LuckyBlock() {

    }

    @Override
    public ItemStack getItem() {
        ItemStack item = ItemUtils.createSkull("4b92cb43333aa621c70eef4ebf299ba412b446fe12e341ccc582f3192189");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Lucky Block");
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLuckyBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!this.getItem().isSimilar(e.getPlayer().getItemInHand()))
            return;

        e.getBlock().removeMetadata("playerPlaced", Main.getPlugin(Main.class));

        ArmorStand stand = Util.spawnStand(e.getBlock().getLocation().add(0.5, -1, 0.5));

        List<ItemStack> possibleDrops = Arrays.stream(Category.values())
                .flatMap(category -> Arrays.stream(category.getItems(plugin.getMapManager().getCurrentMap(), player)))
                .map(ShopItem::getItem)
                .collect(Collectors.toList());
        int dropIndex = new Random().nextInt(Math.toIntExact(possibleDrops.size()));
        ItemStack drop = possibleDrops.get(dropIndex);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            int timeRemaining = 3;

            @Override
            public void run() {
                if (timeRemaining <= 0) {
                    stand.remove();
                    e.getBlock().setType(Material.AIR);

                    ItemUtils.dropItem(e.getBlock().getLocation().add(0.5, 0, 0.5), drop);

                    this.cancel();
                    return;
                }

                stand.setCustomName("" + ChatColor.YELLOW + timeRemaining);

                timeRemaining--;
            }
        }, 0, 20);
    }
}
