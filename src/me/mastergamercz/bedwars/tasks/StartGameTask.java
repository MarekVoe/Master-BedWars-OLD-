package me.mastergamercz.bedwars.tasks;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.utils.ActionAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameTask extends BukkitRunnable {

    private Main plugin;
    private int timeLeft;

    public StartGameTask(Main plugin, int timeLeft) {
        this.timeLeft = timeLeft;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        timeLeft--;
        if (timeLeft  <= 0) {
            this.cancel();
            plugin.startGame();
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            ActionAPI.sendAnnouncement(p, ChatColor.GREEN + "Starting in: " + timeLeft + " seconds");
        }
        if (timeLeft == 5) {
            String winner = plugin.getVotingManager().getWinner();
            plugin.getMapManager().selectMap(winner);
            plugin.loadMap(winner);

            plugin.getVotingManager().end();
        }
    }
}
