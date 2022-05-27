package me.mastergamercz.bedwars.commands;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.managers.VotingManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor {

    private final VotingManager votingManager;
    private Main plugin;

    public VoteCommand(Main plugin, VotingManager votingManager) {
        this.votingManager = votingManager;
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!votingManager.isRunning()) {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + " Voting has already ended.");
        } else if (args.length == 0) {
            listMaps(sender);
        } else if (!votingManager.vote(sender, args[0])) {
            listMaps(sender);
        }
        return true;
    }

    private void listMaps(CommandSender sender) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.GOLD + " These maps are available:");
        int count = 0;
        for (String map : votingManager.getMaps().values()) {
            count ++;
            sender.sendMessage(ChatColor.DARK_GRAY + " - " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + count + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD + map);
        }
    }
}
