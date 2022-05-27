package me.mastergamercz.bedwars.commands;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedWarsCommand implements CommandExecutor {

    private final Main instance;

    public BedWarsCommand(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {
            if (args.length == 0) {
                 sendHelp(sender);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    instance.reloadConfig();
                    instance.getConfigManager().reload("messages.yml");
                    instance.getConfigManager().reload("config.yml");
                    instance.getConfigManager().reload("stats.yml");
                    instance.getConfigManager().reload("maps.yml");
                    instance.getScoreboardHandler().update();
                    sender.sendMessage(instance.getPrefix() +  ChatColor.translateAlternateColorCodes('§', instance.getConfigManager().getConfig("messages.yml").getString("reload-message")));
                } else if (args[0].equalsIgnoreCase("start")) {
                    if (sender.hasPermission("bedwars.start")) {
                        instance.getStartGameTask().runTaskTimer(instance, 0,20);
                    } else {
                        sender.sendMessage(instance.getPrefix() + ChatColor.translateAlternateColorCodes('§', instance.getConfigManager().getConfig("messages.yml").getString("permission-error")));
                    }
                } else if (args[0].equalsIgnoreCase("stats")) {
                    sender.sendMessage(instance.getPrefix() + ChatColor.GOLD + "Kills: " + ChatColor.WHITE + PlayerMeta.getMeta((Player) sender).getKills());
                    sender.sendMessage(instance.getPrefix() + ChatColor.GOLD + "Deaths: " + ChatColor.WHITE + PlayerMeta.getMeta((Player)sender).getDeaths());
                    sender.sendMessage(instance.getPrefix() + ChatColor.GOLD + "Beds Destroyed: " + ChatColor.WHITE + PlayerMeta.getMeta((Player) sender).getBeds_destroyed());
                    sender.sendMessage(instance.getPrefix() + ChatColor.GOLD + "Wins: " + ChatColor.WHITE + PlayerMeta.getMeta((Player) sender).getWins());
                    sender.sendMessage(instance.getPrefix() + ChatColor.GOLD + "Losses: " + ChatColor.WHITE + PlayerMeta.getMeta((Player) sender).getLosses());
                }


            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("restore")) {
                    instance.endGame();
                    ((Player) sender).kickPlayer(ChatColor.GREEN + "Resetting...");
                } else if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("activate")) {
                    if (!instance.getAdmins().contains(player.getUniqueId())) {
                        instance.getAdmins().add(player.getUniqueId());
                        player.sendMessage(instance.getPrefix() + ChatColor.RED + "Admin mode" + ChatColor.GREEN + " activated");
                    } else  {
                        instance.getAdmins().remove(player.getUniqueId());
                        player.sendMessage(instance.getPrefix() + ChatColor.RED + "Admin mode deactivated");
                    }
                }
            }
        }

        return false;
    }

    private void sendHelp(CommandSender sender) {
         sender.sendMessage(instance.getPrefix() + ChatColor.DARK_GRAY + " - " + ChatColor.GOLD + "/bw" + ChatColor.WHITE + "   Basic usage");
         sender.sendMessage(instance.getPrefix() + ChatColor.DARK_GRAY + " - " + ChatColor.GOLD + "/bw reload" + ChatColor.WHITE + "   Reloads bedwars plugin");
         sender.sendMessage(instance.getPrefix() + ChatColor.DARK_GRAY + " - " + ChatColor.GOLD + "/bw start" + ChatColor.WHITE + "   Starts game");
    }
}
