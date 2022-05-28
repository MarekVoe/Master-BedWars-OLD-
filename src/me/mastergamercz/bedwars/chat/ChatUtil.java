package me.mastergamercz.bedwars.chat;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

    private static Main plugin = Main.getPlugin(Main.class);

    public static void allMessage(Team team, Player sender, String message) {

        String username;
        String group;

        group = (ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "All" + ChatColor.DARK_GRAY + "]");
        username = team.color() + sender.getName();


        String msg = message;
        // Todo Vault Hook

        String toSend = group + " " + username + ChatColor.GOLD + " " + ChatColor.BOLD +  "> " +  ChatColor.RESET + msg;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(toSend);
        }
    }

    public static void teamMessage(Team team, Player sender, String message) {
        String group;

        group = (ChatColor.DARK_GRAY + "[" + team.color() + team.toString() + ChatColor.DARK_GRAY + "]");

        String toSend = group + " " + team.color() + sender.getName() + ChatColor.GOLD + " " + ChatColor.BOLD + "> " + ChatColor.RESET + message;
        for (Player player : team.getPlayers())
            player.sendMessage(toSend);
    }

    public static void winMessage(Team winner) {
         Bukkit.broadcastMessage(plugin.getPrefix() + winner.getColor() + winner.toString() + ChatColor.GOLD + " has won the game !");

    }

    public static String formatDeathMessage(Player victim, Player killer,
                                            String original) {
        Team killerTeam = PlayerMeta.getMeta(killer).getTeam();
        String killerColor = killerTeam != null ? killerTeam.color().toString()
                : ChatColor.DARK_PURPLE.toString();
        String killerName = killerColor + killer.getName() + ChatColor.GRAY;

        String message = ChatColor.GRAY + formatDeathMessage(victim, original);
        message = message.replace(killer.getName(), killerName);

        return message;
    }

    public static String formatDeathMessage(Player victim, String original) {
        Team victimTeam = PlayerMeta.getMeta(victim).getTeam();
        String victimColor = victimTeam != null ? victimTeam.color().toString()
                : ChatColor.DARK_PURPLE.toString();
        String victimName = victimColor + victim.getName() + ChatColor.DARK_GRAY;

        String message = ChatColor.DARK_GRAY + original;
        message = message.replace(victim.getName(), victimName);

        if (message.contains(" �8�")) {
            String[] arr = message.split(" �8�");
            message = arr[0];
        }

        return message.replace("was slain by", "was killed by");
    }

}
