package me.mastergamercz.bedwars.managers;


import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private Connection connection;
    private String host, database,username,password;
    private int port;
    private Main plugin;

    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
    }


    public void setup() {
         host = plugin.getConfigManager().getConfig("config.yml").getString("MySQL.host");
         port = plugin.getConfigManager().getConfig("config.yml").getInt("MySQL.port");
         database = plugin.getConfigManager().getConfig("config.yml").getString("MySQL.name");
         password = plugin.getConfigManager().getConfig("config.yml").getString("MySQL.pass");
         username = plugin.getConfigManager().getConfig("config.yml").getString("MySQL.user");

         try {
             if (getConnection() != null && !getConnection().isClosed()) {
                 return;
             }

             Class.forName("com.mysql.jdbc.Driver");
             setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":"
             + port + "/" + database, username, password));

             Bukkit.getConsoleSender().sendMessage(plugin.getPrefix() + ChatColor.GREEN + "MySQL connected !");


         } catch(SQLException e) {
             e.printStackTrace();
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
         }
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM stats WHERE UUID=?");
            statement.setString(1, uuid.toString());


            ResultSet results = statement.executeQuery();

            if (results.next()) {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Player found: " + uuid.toString());
                return true;
            }
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Player not found: " + uuid.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateDeaths(Player player, UUID uuid) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE stats SET DEATHS=? WHERE UUID=?");
            statement.setInt(1, PlayerMeta.getMeta(player).getDeaths() + 1);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            PreparedStatement getStatement = getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, uuid.toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setDeaths(results.getInt("DEATHS"));

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKills(Player player, UUID uuid) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE stats SET KILLS=? WHERE UUID=?");
            statement.setInt(1, PlayerMeta.getMeta(player).getKills() + 1);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            PreparedStatement getStatement = getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, uuid.toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setKills(results.getInt("KILLS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWins(Player player, UUID uuid) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE stats SET WINS=? WHERE UUID=?");
            statement.setInt(1, PlayerMeta.getMeta(player).getWins() + 1);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            PreparedStatement getStatement = getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, uuid.toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setWins(results.getInt("WINS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLosses(Player player, UUID uuid) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE stats SET LOSSES=? WHERE UUID=?");
            statement.setInt(1, PlayerMeta.getMeta(player).getWins() + 1);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            PreparedStatement getStatement = getConnection()
                    .prepareStatement("SELECT * FROM stats WHERE UUID=?");
            getStatement.setString(1, uuid.toString());
            ResultSet results = getStatement.executeQuery();
            results.next();
            PlayerMeta.getMeta(player).setWins(results.getInt("LOSSES"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBeds(Player player, UUID uuid) {
        try {
           PreparedStatement statement = getConnection()
                   .prepareStatement("UPDATE stats SET BEDS_DESTROYED=? WHERE UUID=?");
           statement.setInt(1, PlayerMeta.getMeta(player).getBeds_destroyed() + 1);
           statement.setString(2, uuid.toString());
           statement.executeUpdate();


           PreparedStatement getStatement = getConnection()
                   .prepareStatement("SELECT * FROM stats WHERE UUID=?");
           getStatement.setString(1, uuid.toString());
           ResultSet results = getStatement.executeQuery();
           results.next();
           PlayerMeta.getMeta(player).setBeds_destroyed(results.getInt("BEDS_DESTROYED"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(final UUID uuid, Player player) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM stats WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!playerExists(uuid)) {
                PreparedStatement insert = getConnection().prepareStatement("INSERT INTO stats (UUID,KILLS,DEATHS,BEDS_DESTROYED,WINS,LOSSES) VALUE (?,?,?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setInt(2, 0);
                insert.setInt(3, 0);
                insert.setInt(4, 0);
                insert.setInt(5, 0);
                insert.setInt(6, 0);
                insert.executeUpdate();

                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Player inserted");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
