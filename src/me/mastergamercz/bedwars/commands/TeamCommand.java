package me.mastergamercz.bedwars.commands;

import me.mastergamercz.bedwars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommand implements CommandExecutor {

    private final Main plugin;


    public TeamCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Team, you want to join doesnt exist !");
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Invalid arguments !");
            } else {
                plugin.joinTeam((Player) sender, args[0]);
            }
        }
        return false;
    }
}
