package me.mastergamercz.bedwars.tasks;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.utils.ActionAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartGameTask extends BukkitRunnable {

    private Main plugin;
    private int timeLeft;

    public RestartGameTask(Main plugin, int timeLeft) {
        this.timeLeft = timeLeft;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        timeLeft--;
        if (timeLeft  <= 0) {
            this.cancel();
            plugin.endGame();
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            ActionAPI.sendAnnouncement(p, ChatColor.RED + "Restarting in: " + timeLeft + " seconds");
        }
    }
}

