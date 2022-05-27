package me.mastergamercz.bedwars.managers;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.enums.StatType;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsManager {

    private StatType statType;
    private Main plugin;

    public StatsManager(Main plugin) {
        this.plugin = plugin;
    }

    public void setStatType(StatType statType, Player player) {
        this.statType = statType;

        switch(statType) {
            case KILLS:
               plugin.getDatabaseManager().updateKills(player, player.getUniqueId());
            break;

            case DEATHS:
            plugin.getDatabaseManager().updateDeaths(player, player.getUniqueId());
            break;

            case WINS:
               plugin.getDatabaseManager().updateWins(player, player.getUniqueId());
            break;

            case LOSSES:
               plugin.getDatabaseManager().updateLosses(player, player.getUniqueId());
            break;

            case BEDS_DESTROYED:
                plugin.getDatabaseManager().updateBeds(player, player.getUniqueId());
            break;
        }
    }

    public void loadStats(Player player) {
        // Load Kills
         try {
             PreparedStatement getStatement = plugin.getDatabaseManager().getConnection()
                     .prepareStatement("SELECT * FROM stats WHERE UUID=?");
             getStatement.setString(1, player.getUniqueId().toString());
             ResultSet results = getStatement.executeQuery();
             results.next();
             PlayerMeta.getMeta(player).setKills(results.getInt("KILLS"));
         } catch (SQLException e) {
             e.printStackTrace();
         }


         // Load Deaths
        try {
            PreparedStatement getStatement = plugin.getDatabaseManager().getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, player.getUniqueId().toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setDeaths(results.getInt("DEATHS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load Beds Destroyed
        try {
            PreparedStatement getStatement = plugin.getDatabaseManager().getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, player.getUniqueId().toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setBeds_destroyed(results.getInt("BEDS_DESTROYED"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load Wins
        try {
            PreparedStatement getStatement = plugin.getDatabaseManager().getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, player.getUniqueId().toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setWins(results.getInt("WINS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Load Losses
        try {
            PreparedStatement getStatement = plugin.getDatabaseManager().getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, player.getUniqueId().toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setLosses(results.getInt("LOSSES"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
