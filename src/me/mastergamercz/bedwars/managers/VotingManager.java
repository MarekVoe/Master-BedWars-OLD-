package me.mastergamercz.bedwars.managers;

import me.mastergamercz.bedwars.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class VotingManager {

    private final Main plugin;
    private final HashMap<Integer, String> maps = new HashMap<Integer, String>();
    private final HashMap<String, String> votes = new HashMap<String, String>();
    private boolean running = false;

    public VotingManager(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        maps.clear();
        votes.clear();

        running = true;

        plugin.getScoreboardHandler().update();
    }

    public boolean vote(CommandSender voter, String vote) {
        try {
            int val = Integer.parseInt(vote);

            if (maps.containsKey(val)) {
                vote = maps.get(val);
                for (String map : maps.values()) {
                    if (vote.equalsIgnoreCase(map)) {
                        votes.put(voter.getName(), map);
                        voter.sendMessage(plugin.getPrefix() + ChatColor.GOLD + "You voted for " + ChatColor.WHITE + map);
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            for (String map : maps.values()) {
                if (vote.equalsIgnoreCase(map)) {
                    votes.put(voter.getName(), map);
                    voter.sendMessage(plugin.getPrefix() + ChatColor.GOLD + "You voted for " + ChatColor.WHITE + map);
                    return true;
                }
            }
        }

        voter.sendMessage(plugin.getPrefix() + ChatColor.GOLD + vote + ChatColor.RED + " is not a valid map.");
        return false;
    }

    public String getWinner() {
        String winner = null;
        Integer highest = -1;
        for (String map : maps.values()) {
            int totalVotes = countVotes(map);
            if (totalVotes > highest) {
                winner = map;
                highest = totalVotes;
            }
        }
        return winner;
    }

    public void end() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public HashMap<Integer, String> getMaps() {
        return maps;
    }

    private int countVotes(String map) {
        int total = 0;
        for (String vote : votes.values())
            if (vote.equals(map))
                total++;
        return total;
    }

    private void updateScoreboard() {
        for (String map : maps.values()) {
            plugin.getScoreboardHandler().teams.get(map).setSuffix(ChatColor.WHITE + " » " + ChatColor.GOLD + countVotes(map) + " Vote" + (countVotes(map) == 1 ? "" : "s"));
        }
    }
}

