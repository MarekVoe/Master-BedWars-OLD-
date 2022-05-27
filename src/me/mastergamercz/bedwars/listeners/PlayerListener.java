package me.mastergamercz.bedwars.listeners;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.chat.ChatUtil;
import me.mastergamercz.bedwars.enums.StatType;
import me.mastergamercz.bedwars.enums.Team;
import me.mastergamercz.bedwars.utils.Board;
import me.mastergamercz.bedwars.utils.Util;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Iterator;

public class PlayerListener implements Listener {

    private final Main instance;

    public PlayerListener(Main instance) {
        this.instance = instance;
    }

     @EventHandler
     public void onMOTDPing(ServerListPingEvent e) {
        if (instance.motd) {
            String motd = instance.getConfigManager().getConfig("config.yml").getString("motd");
            try {
                motd = motd.replaceAll("%PLAYERCOUNT",
                        String.valueOf(Bukkit.getOnlinePlayers().size()));
                motd = motd.replaceAll("%MAXPLAYERS%",
                        String.valueOf(Bukkit.getMaxPlayers()));
                motd = motd.replaceAll("%GREENCOUNT%",
                        String.valueOf(getPlayers(Team.GREEN)));
                motd = motd.replaceAll("%REDCOUNT%",
                        String.valueOf(getPlayers(Team.GREEN)));
                motd = motd.replaceAll("%BLUECOUNT%",
                        String.valueOf(getPlayers(Team.GREEN)));
                motd = motd.replaceAll("%YELLOWCOUNT%",
                        String.valueOf(getPlayers(Team.GREEN)));
                motd = motd.replaceAll("%PLAYERS%",
                        String.valueOf(Bukkit.getOnlinePlayers().size()));

                e.setMotd(ChatColor.translateAlternateColorCodes('§', motd));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
     }

    private int getPlayers(Team t) {
        int size = 0;

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerMeta meta = PlayerMeta.getMeta(p);
            if (meta.getTeam() == t) {
                size++;
            }
        }

        return size;
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        if (instance.getPlacedBlocks().contains(block.getLocation())) {
            e.setCancelled(false);
        }

        if (e.getBlock().getType() == Material.BED || e.getBlock().getType() != Material.BED_BLOCK) {
            e.setCancelled(true);
        } else if (e.getBlock().getType() == Material.BED || e.getBlock().getType() != Material.BED_BLOCK && instance.getAdmins().contains(player.getUniqueId())) {
           e.setCancelled(false);
        } else {
            for (Team team : Team.teams()) {
                if (team.getTeamBed().getLocation().equals(e.getBlock().getLocation()) || team.getTeamBed().getLocation().distance(e.getBlock().getLocation()) <= 3) {
                    final Team attacker = PlayerMeta.getMeta(player).getTeam();
                    if (team == attacker) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You cannot destroy your own bed !");
                    } else {
                        breakBed(team, player);
                    }
                }
            }
        }
    }

    public void breakBed(final Team victim, Player breaker) {
         final Team attacker = PlayerMeta.getMeta(breaker).getTeam();
             Bukkit.broadcastMessage(instance.getPrefix() + victim.getColor() + victim.toString() + ChatColor.RED + " bed was destroyed !");
             victim.getTeamBed().setAlive(false);
             instance.getStatsManager().setStatType(StatType.BEDS_DESTROYED, breaker);
             instance.checkWin();

      instance.getScoreboardHandler().scores.clear();
      instance.getScoreboardHandler().teams.clear();

        instance.getScoreboardHandler().sb = Bukkit.getScoreboardManager().getNewScoreboard();
        instance.getScoreboardHandler().obj = instance.getScoreboardHandler().sb.registerNewObjective("bedwars", "dummy");
        instance.getScoreboardHandler().obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        instance.getScoreboardHandler().obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "BedWars");


        Score map = instance.getScoreboardHandler().obj.getScore(ChatColor.DARK_GRAY + "Map: " + ChatColor.GOLD + instance.getVotingManager().getWinner());
        Score null1 = instance.getScoreboardHandler().obj.getScore("");
        Score null2 = instance.getScoreboardHandler().obj.getScore(" ");

        map.setScore(9);
        null1.setScore(8);
        null2.setScore(0);



       int size = 6;

       for (Team teams : Team.teams()) {
           size--;
           if (teams.getTeamBed().isAlive()) {
               instance.getScoreboardHandler().scores.put(teams.name(), instance.getScoreboardHandler().obj.getScore(ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + teams.getPlayers().size() + ChatColor.DARK_GRAY + ") " + ChatColor.RED + "\u2764 " + teams.getColor() + WordUtils.capitalize(teams.name().toLowerCase() + " Team")));
               instance.getScoreboardHandler().scores.get(teams.name()).setScore(size);
           }
       }

       instance.getScoreboardHandler().update();

        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.ENDERDRAGON_GROWL, 1,1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getEntity().getWorld().getName().equalsIgnoreCase("lobby")) {
                e.setCancelled(true);

                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.getEntity().teleport(instance.getMapManager().getLobbySpawnPoint());
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        final Player player = (Player) e.getEntity();
        e.setDeathMessage(ChatUtil.formatDeathMessage(player, e.getDeathMessage()));
        if (PlayerMeta.getMeta(player).getTeam().getTeamBed().isAlive()) {
            player.setHealth(20);
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(instance.getPrefix() + ChatColor.GOLD + "You will respawn in 5 seconds...");
            player.teleport(PlayerMeta.getMeta(player).getTeam().getRandomSpawn());
            Bukkit.getScheduler().scheduleSyncDelayedTask(this.instance, new Runnable() {

                @Override
                public void run() {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(PlayerMeta.getMeta(player).getTeam().getRandomSpawn());
                    player.sendMessage(instance.getPrefix() + ChatColor.GREEN + "You have respawned.");
                    instance.getStatsManager().setStatType(StatType.DEATHS, player);
                }
            }, 100L);
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(instance.getPrefix() + ChatColor.RED + "Your bed no longer exists. You have been sent to spectate mode");
            PlayerMeta.getMeta(player).setTeam(Team.NONE);
            instance.getStatsManager().setStatType(StatType.LOSSES, player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        instance.getDatabaseManager().createPlayer(player.getUniqueId(), player);
        instance.getStatsManager().loadStats(player);

        instance.getScoreboardHandler().setLobbyScoreboard();

        if (instance.hasStarted && !player.hasPermission("bedwars.join.bypass")) {
            player.kickPlayer(ChatColor.RED + "Game has started. If you want to spectate, purchase " + ChatColor.GOLD + "" + ChatColor.BOLD + "Premium");

        } else if (instance.hasStarted && player.hasPermission("bedwars.join.bypass")) {
            player.sendMessage(instance.getPrefix() + ChatColor.GREEN + "Game has already started, you have been sent to spectate mode.");
            player.getInventory().clear();
            setGameScoreboard(player);

            Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
                @Override
                public void run(){
                    player.setGameMode(GameMode.SPECTATOR);
                    e.setJoinMessage(null);
                }
            }, 3L);


        } else if (!instance.hasStarted) {

            e.setJoinMessage(instance.getPrefix() + " " + ChatColor.GOLD + player.getName() + ChatColor.DARK_GRAY + " has joined the game.");

            player.getInventory().clear();
            giveLobbyItems(player);
            player.teleport(instance.getMapManager().getLobbySpawnPoint());


            instance.getScoreboardHandler().update();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        e.setQuitMessage(instance.getPrefix() + " " + ChatColor.GOLD + player.getName() + ChatColor.DARK_GRAY + " has joined the game.");
    }

    public void giveLobbyItems(Player player) {
        Inventory inv = player.getInventory();
        ItemStack teamSelector = new ItemStack(Material.BANNER);
        ItemMeta teamMeta = teamSelector.getItemMeta();

        teamMeta.setDisplayName(ChatColor.WHITE + "Team Selector");
        teamSelector.setItemMeta(teamMeta);

        inv.addItem(teamSelector);
    }

    @EventHandler
    public void lobbyItemsRClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("lobby") && player.getItemInHand().getType().equals(Material.BANNER)) {
            e.setCancelled(true);
            openTeamSelector(player);
        }
    }

    public void openTeamSelector(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Team Selector");
        ItemStack redTeam = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta redMeta = redTeam.getItemMeta();
        redMeta.setDisplayName(ChatColor.RED + "Red Team");
        redTeam.setItemMeta(redMeta);

        ItemStack blueTeam = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta blueMeta = blueTeam.getItemMeta();
        blueMeta.setDisplayName(ChatColor.BLUE + "Blue Team");
        blueTeam.setItemMeta(blueMeta);

        ItemStack greenTeam = new ItemStack(Material.WOOL, 1, (short) 13);
        ItemMeta greenMeta = greenTeam.getItemMeta();;
        greenMeta.setDisplayName(ChatColor.GREEN + "Green Team");
        greenTeam.setItemMeta(greenMeta);


        ItemStack yellowTeam = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta yellowMeta = yellowTeam.getItemMeta();
        yellowMeta.setDisplayName(ChatColor.YELLOW + "Yellow Team");
        yellowTeam.setItemMeta(yellowMeta);


        inv.addItem(redTeam);
        inv.addItem(blueTeam);
        inv.addItem(greenTeam);
        inv.addItem(yellowTeam);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack clickedItem = e.getCurrentItem();

        if (player.getWorld().getName().equalsIgnoreCase("lobby") && inv.getName().equalsIgnoreCase("Team Selector")) {
            if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Red Team")) {
                player.performCommand("team red");
                e.setCancelled(true);
                player.closeInventory();
            } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "Blue Team")) {
                player.performCommand("team blue");
                e.setCancelled(true);
                player.closeInventory();
            } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Green Team")) {
                player.performCommand("team green");
                e.setCancelled(true);
                player.closeInventory();
            } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.YELLOW + "Yellow Team")) {
                player.performCommand("team yellow");
                e.setCancelled(true);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInteractShop(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Villager villager = (Villager) e.getRightClicked();
        if (e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            e.setCancelled(true);
              instance.getItemShop().openShop(player);
              System.out.println(player.getName() + " Interacted with villager");
        } else {
            player.sendMessage(ChatColor.RED + "This is not a villager");
        }
    }

    public void setGameScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager sbm = Bukkit.getScoreboardManager();
        Scoreboard sb = sbm.getNewScoreboard();

        Objective obj = sb.registerNewObjective("bwgame", "dummy");

        obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "BedWars");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score map = obj.getScore(ChatColor.DARK_GRAY + "Map: " + ChatColor.GOLD + instance.getVotingManager().getWinner());
        Score null1 = obj.getScore("");
        Score blueTeam = obj.getScore(ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + Team.BLUE.getPlayers().size() + ChatColor.DARK_GRAY + ") " + ChatColor.RED + "\u2764 " + ChatColor.BLUE + "Blue Team");
        Score null2 = obj.getScore(" ");
        Score redTeam = obj.getScore(ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + Team.RED.getPlayers().size() + ChatColor.DARK_GRAY + ") " + ChatColor.RED + "\u2764 Red Team");
        Score null3 = obj.getScore("  ");
        Score greenTeam = obj.getScore(ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + Team.GREEN.getPlayers().size() + ChatColor.DARK_GRAY + ") " + ChatColor.RED + "\u2764 " + ChatColor.GREEN + "Green Team");
        Score null4 = obj.getScore("   ");
        Score yellowTeam = obj.getScore(ChatColor.DARK_GRAY + "(" + ChatColor.WHITE + Team.YELLOW.getPlayers().size() + ChatColor.DARK_GRAY + ") " + ChatColor.RED + "\u2764 " + ChatColor.YELLOW + "Yellow Team");
        Score null5 = obj.getScore("    ");

        map.setScore(9);
        null1.setScore(8);
        blueTeam.setScore(6);
        redTeam.setScore(4);
        greenTeam.setScore(2);
        yellowTeam.setScore(1);
        null5.setScore(0);

        player.setScoreboard(sb);
    }

    @EventHandler
    public void noUproot(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
            event.setCancelled(true);
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase("lobby")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();

        if (inv == player.getInventory()) {
            e.setCancelled(false);
        } else if (inv.getName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Shop")) {
            e.setCancelled(true);
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Blocks")) {
                 // TODO
            } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Tools")) {
                // TODO
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        Player player = e.getPlayer();

        if (item.getType().equals(Material.BED) || item.getType().equals(Material.BED_BLOCK)) {
            player.getInventory().removeItem(item);
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("lobby")) {
            e.setCancelled(true);
        } else {
         instance.getPlacedBlocks().add(e.getBlock().getLocation());
        }
    }
}
