package me.mastergamercz.bedwars;

import me.mastergamercz.bedwars.chat.ChatListener;
import me.mastergamercz.bedwars.chat.ChatUtil;
import me.mastergamercz.bedwars.commands.BedWarsCommand;
import me.mastergamercz.bedwars.commands.TeamCommand;
import me.mastergamercz.bedwars.commands.VoteCommand;
import me.mastergamercz.bedwars.enums.GameStatus;
import me.mastergamercz.bedwars.enums.GenType;
import me.mastergamercz.bedwars.enums.StatType;
import me.mastergamercz.bedwars.enums.Team;
import me.mastergamercz.bedwars.gens.Bronze_ItemGenerator;
import me.mastergamercz.bedwars.gens.Gold_ItemGenerator;
import me.mastergamercz.bedwars.gens.Iron_ItemGenerator;
import me.mastergamercz.bedwars.listeners.EntityListener;
import me.mastergamercz.bedwars.listeners.MenuListeners;
import me.mastergamercz.bedwars.listeners.PlayerListener;
import me.mastergamercz.bedwars.listeners.WorldListener;
import me.mastergamercz.bedwars.managers.*;
import me.mastergamercz.bedwars.maps.MapRollback;
import me.mastergamercz.bedwars.tasks.RestartGameTask;
import me.mastergamercz.bedwars.tasks.StartGameTask;
import me.mastergamercz.bedwars.utils.Util;
import me.mastergamercz.bedwars.utils.WorldUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin {

    private String prefix;
    private ConfigManager configManager;
    private MapManager mapManager;
    private ScoreboardManager sb;
    private VotingManager votingManager;
    private SoundManager soundManager;
    private DatabaseManager databaseManager;
    public boolean hasStarted = false;
    private StartGameTask startGameTask;
    public boolean motd = true;
    private ArrayList<Location> bronzes = new ArrayList<Location>();
    private ArrayList<Location> irons = new ArrayList<Location>();
    private ArrayList<Location> golds = new ArrayList<Location>();
    private StatsManager statsManager;
    private WorldUtils worldUtils;
    private ArrayList<Location> villagers = new ArrayList<Location>();
    private ArrayList<UUID> admins = new ArrayList<UUID>();
    private RestartGameTask restartGameTask;
    private GameStatus gameStatus;
    private ArrayList<Location> placedBlocks = new ArrayList<Location>();
    private ArrayList<UUID> vulnerable = new ArrayList<UUID>();

    public void onEnable() {
         configManager = new ConfigManager(this);
         configManager.loadConfigFiles("config.yml", "maps.yml", "messages.yml", "stats.yml");
         MapRollback mapRollback = new MapRollback(getLogger(), getDataFolder());
         mapManager = new MapManager(this, mapRollback, configManager.getConfig("maps.yml"));
         votingManager = new VotingManager(this);
         this.startGameTask = new StartGameTask(this, getConfig().getInt("start-delay"));
         this.restartGameTask = new RestartGameTask(this, getConfig().getInt("restart-delay"));
         sb = new ScoreboardManager(this);
         this.soundManager = new SoundManager();
         this.databaseManager = new DatabaseManager(this);
         this.statsManager = new StatsManager(this);
         this.worldUtils = new WorldUtils();

         motd = configManager.getConfig("config.yml").getBoolean("enableMotd", true);
         setGameStatus(GameStatus.LOBBY);
         setPrefix(ChatColor.translateAlternateColorCodes('&', getConfigManager().getConfig("messages.yml").getString("prefix")));
         databaseManager.setup();

         registerCommands();
         registerListeners();
         reset();
    }

    public void reset() {
        getScoreboardHandler().setLobbyScoreboard();
        mapManager.reset();
        PlayerMeta.reset();

        for (Player p : getServer().getOnlinePlayers()) {
            PlayerMeta.getMeta(p).setTeam(Team.NONE);
            p.teleport(mapManager.getLobbySpawnPoint());
            p.setMaxHealth(20D);
            p.setHealth(20D);
            p.setFoodLevel(20);
            p.setSaturation(20F);
        }
        votingManager.start();
        sb.update();
    }

    public void endGame() {
        try {
            resetMap(new File(getServer().getWorldContainer() +  "/" + mapManager.getCurrentMap().getName()), new File(getDataFolder().getAbsolutePath() + "/MapReset/" + mapManager.getCurrentMap().getName()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void resetMap(File sourceMap, File dest) throws IOException {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.kickPlayer(ChatColor.RED + "Resetting map...");
        }
           this.worldUtils.rollback(getVotingManager().getWinner());
           hasStarted = false;

           Bukkit.getScheduler().cancelAllTasks();
           PlayerMeta.reset();
           mapManager.reset();
           placedBlocks.clear();
        for (Entity e : Bukkit.getWorld(votingManager.getWinner()).getEntities()) {
            e.remove();
        }
        getServer().reload();
    }

    public void registerCommands() {
       getServer().getPluginCommand("bw").setExecutor(new BedWarsCommand(this));
       getServer().getPluginCommand("team").setExecutor(new TeamCommand(this));
       getServer().getPluginCommand("vote").setExecutor(new VoteCommand(this, this.votingManager));
    }

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new WorldListener(this),this);
        pm.registerEvents(new ChatListener(this),this);
        pm.registerEvents(new EntityListener(this),this);
        pm.registerEvents(new MenuListeners(),this);
    }

    public void startGame() {
        Bukkit.broadcastMessage(getPrefix() + ChatColor.translateAlternateColorCodes('&', getConfigManager().getConfig("messages.yml").getString("game-started")));
      for (Player p : getServer().getOnlinePlayers()) {
          if (PlayerMeta.getMeta(p).getTeam() != Team.NONE) {
              Util.sendPlayersToGame(p, this);
          }
      }
      hasStarted = true;

        getScoreboardHandler().scores.clear();
        getScoreboardHandler().teams.clear();

        getScoreboardHandler().sb = Bukkit.getScoreboardManager().getNewScoreboard();
        getScoreboardHandler().obj = getScoreboardHandler().sb.registerNewObjective("bedwars", "dummy");
        getScoreboardHandler().obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        getScoreboardHandler().obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "BedWars");


        Score map = getScoreboardHandler().obj.getScore(ChatColor.DARK_GRAY + "Map: " + ChatColor.GOLD + getVotingManager().getWinner());
        Score null1 = getScoreboardHandler().obj.getScore("");
        Score null2 = getScoreboardHandler().obj.getScore(" ");

        map.setScore(9);
        null1.setScore(8);
        null2.setScore(0);

        int size = 6;

        for (Team teams : Team.teams()) {
            size--;
            if (teams.getTeamBed().isAlive()) {
                getScoreboardHandler().scores.put(teams.name(), getScoreboardHandler().obj.getScore(ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + teams.getPlayers().size() + ChatColor.DARK_GRAY + ") " + ChatColor.RED + "\u2764 " + teams.getColor() + WordUtils.capitalize(teams.name().toLowerCase() + " Team")));
                getScoreboardHandler().scores.get(teams.name()).setScore(size);
            }
        }

        getScoreboardHandler().setTeam(Team.BLUE);
        getScoreboardHandler().setTeam(Team.RED);
        getScoreboardHandler().setTeam(Team.GREEN);
        getScoreboardHandler().setTeam(Team.YELLOW);
        getScoreboardHandler().update();

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (PlayerMeta.getMeta(players).getTeam() == Team.BLUE) {
                getScoreboardHandler().teams.get(Team.BLUE.toString().toUpperCase()).addPlayer(players);
            } else if (PlayerMeta.getMeta(players).getTeam() == Team.RED) {
               getScoreboardHandler().teams.get(Team.RED.toString().toUpperCase()).addPlayer(players);
            } else if (PlayerMeta.getMeta(players).getTeam() == Team.YELLOW) {
                getScoreboardHandler().teams.get(Team.YELLOW.toString().toUpperCase()).addPlayer(players);
            } else if (PlayerMeta.getMeta(players).getTeam() == Team.GREEN) {
                getScoreboardHandler().teams.get(Team.GREEN.toString().toUpperCase()).addPlayer(players);
            }
        }
    }


    public void loadMap(final String map) {
        FileConfiguration config = configManager.getConfig("maps.yml");
        ConfigurationSection section = config.getConfigurationSection(map);

        World w = getServer().getWorld(map);
        for (Team team : Team.teams()) {
            String name = team.name().toLowerCase();
            if (section.contains("spawns." + name)) {
                for (String s : section.getStringList("spawns." + name)) {
                    team.addSpawn(Util.parseLocation(getServer().getWorld(map), s));
                }
            }

            if (section.contains("beds." + name)) {
                Location loc = Util.parseLocation(w, section.getString("beds." + name));
                team.loadTeamBed(loc);
            }
        }

            if (section.contains("bronzes")) {
                Set<Location> bronzes = new HashSet<Location>();
                for (String s : section.getStringList("bronzes")) {
                    bronzes.add(Util.parseLocation(w,s));
                }
             loadBronzes(bronzes);
            }

            if (section.contains("irons")) {
                Set<Location> irons = new HashSet<Location>();
                for (String s : section.getStringList("irons")) {
                    irons.add(Util.parseLocation(w,s));
                }
                loadIrons(irons);
            }

            if (section.contains("golds")) {
                Set<Location> golds = new HashSet<Location>();
                for (String s : section.getStringList("golds")) {
                    golds.add(Util.parseLocation(w,s));
                }
                loadGolds(golds);
            }

            if (section.contains("villagers")) {
                Set<Location> villagers = new HashSet<Location>();
                for (String s : section.getStringList("villagers")) {
                    villagers.add(Util.parseLocation(w, s));
                }
                loadVillagers(villagers);
            }
    }

    public void loadBronzes(Set<Location> bronzeLocations) {
         for (Location loc : bronzeLocations) {
             bronzes.add(loc);
             new Bronze_ItemGenerator(this, GenType.BRONZE, 2, loc).runTaskTimer(this, 0, 1);
         }
    }

    public void loadVillagers(Set<Location> villagerLocations) {
        for (Location loc : villagerLocations) {
            villagers.add(loc);
            Villager villager = (Villager) Bukkit.getWorld(votingManager.getWinner().toString()).spawnEntity(loc, EntityType.VILLAGER);
            villager.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "Shop");
            villager.setCustomNameVisible(true);
        }
    }

    public void loadIrons(Set<Location> ironLocations) {
        for (Location loc : ironLocations) {
           irons.add(loc);
            new Iron_ItemGenerator(this, GenType.IRON, 10, loc).runTaskTimer(this, 0,1);
        }
    }

    public void loadGolds(Set<Location> goldLocations) {
        for (Location loc : goldLocations) {
            golds.add(loc);
            new Gold_ItemGenerator(this, GenType.GOLD, 30, loc).runTaskTimer(this, 0, 1);
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void joinTeam(Player player, String team) {
        PlayerMeta playerMeta = PlayerMeta.getMeta(player);
        if (playerMeta.getTeam() != Team.NONE && hasStarted) {
            player.sendMessage(prefix + ChatColor.RED + "Cannot switch teams in-game !");
            return;
        }

        Team target;
        try {
            target = Team.valueOf(team.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(prefix + ChatColor.RED + "Team, you want to join doesn't exist!");
            return;
        }

        player.sendMessage(prefix + ChatColor.DARK_GRAY + "Joined team " + target.coloredName());
        playerMeta.setTeam(target);

        getScoreboardHandler().teams.get(team.toUpperCase()).addPlayer(player);
    }

    public void checkWin() {
        System.out.println("Check Win");
       int alive = 0;
       Team aliveTeam = null;
       for (Team t : Team.teams()) {
           if (t.getTeamBed().isAlive()) {
               alive++;
               aliveTeam = t;
           }
       }

       if (alive == 1) {
           endGame2(aliveTeam);
       }
    }

    public void endGame2(Team winner) {
        if (winner == null) {
            return;
        }

        ChatUtil.winMessage(winner);
        restartGameTask.runTaskTimer(this, 0,20);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (PlayerMeta.getMeta(p).getTeam() == winner) {
                getStatsManager().setStatType(StatType.WINS, p);
            }
        }
    }

    public void onDisable() {

    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public String getPrefix() {
         return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ScoreboardManager getScoreboardHandler() {
        return sb;
    }

    public StartGameTask getStartGameTask() {
        return startGameTask;
    }

    public VotingManager getVotingManager() {
        return votingManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public ArrayList<UUID> getAdmins() {
        return admins;
    }

    public ArrayList<Location> getPlacedBlocks() {
        return placedBlocks;
    }

    public ArrayList<UUID> getVulnerable() {
        return vulnerable;
    }
}
