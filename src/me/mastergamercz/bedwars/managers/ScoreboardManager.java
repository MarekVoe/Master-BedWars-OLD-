package me.mastergamercz.bedwars.managers;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;


import java.util.HashMap;

public class ScoreboardManager {
    public Scoreboard sb;
    public Objective obj;
    private Main plugin;

    public HashMap<String, Score> scores = new HashMap<String, Score>();
    public HashMap<String, org.bukkit.scoreboard.Team> teams = new HashMap<String, org.bukkit.scoreboard.Team>();

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
    }

    public void update() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(sb);
        }
    }

    public void setLobbyScoreboard() {

        scores.clear();
        teams.clear();


        sb = Bukkit.getScoreboardManager().getNewScoreboard();

        obj = sb.registerNewObjective("bedwars", "dummy");
        obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "BedWars");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score server = obj.getScore(ChatColor.DARK_GRAY + "Server: " + ChatColor.WHITE + plugin.getConfigManager().getConfig("config.yml").getString("server-name"));
        Score null1 = obj.getScore("");
        Score players = obj.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Players");
        Score playerSize = obj.getScore(ChatColor.WHITE + "" + Bukkit.getOnlinePlayers().size() + "/16");
        Score null2 = obj.getScore(" ");
        Score mapsScore = obj.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Maps");
        Score null3 = obj.getScore("   ");

        server.setScore(9);
        null1.setScore(8);
        players.setScore(7);
        playerSize.setScore(6);
        null2.setScore(5);
        mapsScore.setScore(4);
        null3.setScore(0);

        int count = 0;
        int size = 4;

        for (String map : plugin.getMapManager().getRandomMaps()) {
            size--;
            count++;
            plugin.getVotingManager().getMaps().put(count, map);
            scores.put(map, plugin.getScoreboardHandler().obj.getScore(map));
            scores.get(map).setScore(size);
            teams.put(map, plugin.getScoreboardHandler().sb.registerNewTeam(map));
            teams.get(map).addEntry(map);
            teams.get(map).setPrefix(ChatColor.GOLD + "" + count + ". " + ChatColor.WHITE);
        }

        setTeam(Team.BLUE);
        setTeam(Team.RED);
        setTeam(Team.GREEN);
        setTeam(Team.YELLOW);

        update();
    }

    public void resetScoreboard(String objName) {
        sb = null;
        obj = null;

        scores.clear();
        teams.clear();

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = sb.registerNewObjective("bedwars", "dummy");

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(objName);

        setTeam(Team.RED);
        setTeam(Team.BLUE);
        setTeam(Team.GREEN);
        setTeam(Team.YELLOW);
    }

    public void setTeam(Team t) {
        teams.put(t.name(), sb.registerNewTeam(t.name()));
        org.bukkit.scoreboard.Team sbt = teams.get(t.name());
        sbt.setAllowFriendlyFire(false);
        sbt.setCanSeeFriendlyInvisibles(true);
        sbt.setPrefix(t.color().toString());


    }
}

