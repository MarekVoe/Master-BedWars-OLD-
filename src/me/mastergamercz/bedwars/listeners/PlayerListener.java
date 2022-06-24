package me.mastergamercz.bedwars.listeners;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.UI.Menu;
import me.mastergamercz.bedwars.chat.ChatUtil;
import me.mastergamercz.bedwars.enums.StatType;
import me.mastergamercz.bedwars.enums.Team;
import me.mastergamercz.bedwars.shop.Category;
import me.mastergamercz.bedwars.shop.ShopMenu;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

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
                motd = motd.replaceAll("%GAMESTATUS%",
                        String.valueOf(instance.getGameStatus().getColor() + instance.getGameStatus().toString()));
                motd = motd.replaceAll("%MAP%",
                        String.valueOf(instance.getVotingManager().getWinner()));

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


        if (!instance.getPlacedBlocks().contains(e.getBlock().getLocation())) {
            e.setCancelled(true);
        } else if (instance.getPlacedBlocks().contains(e.getBlock().getLocation())) {
            e.setCancelled(false);
        }
            for (Team team : Team.teams()) {
                if (team.getTeamBed().getLocation().equals(e.getBlock().getLocation()) || team.getTeamBed().getLocation().distance(e.getBlock().getLocation()) <= 3) {
                    final Team attacker = PlayerMeta.getMeta(player).getTeam();
                    if (team == attacker && e.getBlock().getType() == Material.BED || team == attacker && e.getBlock().getType() == Material.BED_BLOCK) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You cannot destroy your own bed !");
                    } else if (team != attacker && e.getBlock().getType() == Material.BED || team != attacker && e.getBlock().getType() == Material.BED_BLOCK) {
                        breakBed(team, player);
                        e.getBlock().setType(Material.AIR);
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


       instance.getScoreboardHandler().setTeam(Team.BLUE);
       instance.getScoreboardHandler().setTeam(Team.RED);
       instance.getScoreboardHandler().setTeam(Team.YELLOW);
       instance.getScoreboardHandler().setTeam(Team.GREEN);

       for (Player p : Bukkit.getOnlinePlayers()) {
           if (PlayerMeta.getMeta(p).getTeam() == Team.BLUE) {
               instance.getScoreboardHandler().teams.get(Team.BLUE.toString().toUpperCase()).addPlayer(p);
           } else if (PlayerMeta.getMeta(p).getTeam() == Team.RED) {
               instance.getScoreboardHandler().teams.get(Team.RED.toString().toUpperCase()).addPlayer(p);
           } else if (PlayerMeta.getMeta(p).getTeam() == Team.GREEN) {
               instance.getScoreboardHandler().teams.get(Team.GREEN.toString().toUpperCase()).addPlayer(p);
           } else if (PlayerMeta.getMeta(p).getTeam() == Team.YELLOW) {
               instance.getScoreboardHandler().teams.get(Team.YELLOW.toString().toUpperCase()).addPlayer(p);
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
            } else if (instance.getVulnerable().contains(e.getEntity().getUniqueId()) && e.getEntity() instanceof Player) {
                    e.setCancelled(true);
                } else {
                    e.setCancelled(false);
                }
            }
        }


    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Villager) {
            e.setCancelled(true);
            e.setDamage(0.0);
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

        instance.getVulnerable().add(player.getUniqueId());

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
            if (Bukkit.getOnlinePlayers().size() == instance.getConfigManager().getConfig("config.yml").getInt("min-players")) {
                instance.getStartGameTask().runTaskTimer(instance, 0,20);
            }


            instance.getScoreboardHandler().update();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        PlayerMeta.getMeta(player).setTeam(Team.NONE);
        e.setQuitMessage(instance.getPrefix() + " " + ChatColor.GOLD + player.getName() + ChatColor.DARK_GRAY + " has joined the game.");
    }

    public void giveLobbyItems(Player player) {
        Inventory inv = player.getInventory();
        ItemStack teamSelector = new ItemStack(Material.BANNER);
        ItemMeta teamMeta = teamSelector.getItemMeta();

        ItemStack maps = new ItemStack(Material.BOOK);
        ItemMeta mapsMeta = maps.getItemMeta();

        mapsMeta.setDisplayName(ChatColor.GOLD + "Vote for map");
        maps.setItemMeta(mapsMeta);

        teamMeta.setDisplayName(ChatColor.WHITE + "Team Selector");
        teamSelector.setItemMeta(teamMeta);

        inv.addItem(teamSelector);
        inv.addItem(maps);
    }

    @EventHandler
    public void lobbyItemsRClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.getWorld().getName().equalsIgnoreCase("lobby") && player.getItemInHand().getType().equals(Material.BANNER)) {
            e.setCancelled(true);
            openTeamSelector(player);
        } else if (player.getWorld().getName().equalsIgnoreCase("lobby") && player.getItemInHand().getType().equals(Material.BOOK)) {
            e.setCancelled(true);
            openMapVoter(player);
        }
    }

    public void openMapVoter(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Maps");

        int size = -1;
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        List<String> lore = new ArrayList<String>();
        paperMeta.setLore(lore);

        for (String name : instance.getMapManager().getRandomMaps()) {
            size++;
            if (instance.getVotingManager().countVotes(name) > 1 || instance.getVotingManager().countVotes(name) == 0) {
                lore.add(ChatColor.WHITE + String.valueOf(instance.getVotingManager().countVotes(name)) + ChatColor.translateAlternateColorCodes('&', " &6Votes"));
            } else if (instance.getVotingManager().countVotes(name) == 1) {
                lore.add(ChatColor.WHITE + String.valueOf(instance.getVotingManager().countVotes(name)) + ChatColor.translateAlternateColorCodes('&', " &6Vote"));
            }
            paperMeta.setDisplayName(name);
            paperMeta.setLore(lore);
            paper.setItemMeta(paperMeta);
            inv.setItem(size, paper);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        e.setCancelled(true);
        player.setFoodLevel(20);
    }

    public void openTeamSelector(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Team Selector");
        ItemStack redTeam = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta redMeta = redTeam.getItemMeta();
        redMeta.setDisplayName(ChatColor.RED + "Red Team");
        List<String> redLore = new ArrayList<String>();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (PlayerMeta.getMeta(players).getTeam() == Team.RED) {
                redLore.add(String.valueOf(ChatColor.RED + players.getName()));
            }
        }

        redMeta.setLore(redLore);
        redTeam.setItemMeta(redMeta);

        ItemStack blueTeam = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta blueMeta = blueTeam.getItemMeta();
        blueMeta.setDisplayName(ChatColor.BLUE + "Blue Team");
        List<String> blueLore = new ArrayList<String>();

        for (Player players : Bukkit.getOnlinePlayers()) {
          if (PlayerMeta.getMeta(players).getTeam() == Team.BLUE) {
              blueLore.add(String.valueOf(ChatColor.BLUE + players.getName()));
          }
        }

        blueMeta.setLore(blueLore);
        blueTeam.setItemMeta(blueMeta);


        ItemStack greenTeam = new ItemStack(Material.WOOL, 1, (short) 13);
        ItemMeta greenMeta = greenTeam.getItemMeta();
        greenMeta.setDisplayName(ChatColor.GREEN + "Green Team");
        List<String> greenLore = new ArrayList<String>();

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (PlayerMeta.getMeta(players).getTeam() == Team.GREEN) {
                greenLore.add(String.valueOf(ChatColor.GREEN + players.getName()));
            }
        }


        greenMeta.setLore(greenLore);
        greenTeam.setItemMeta(greenMeta);

        ItemStack yellowTeam = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta yellowMeta = yellowTeam.getItemMeta();
        yellowMeta.setDisplayName(ChatColor.YELLOW + "Yellow Team");
        List<String> yellowLore = new ArrayList<String>();


        for (Player players : Bukkit.getOnlinePlayers()) {
            if (PlayerMeta.getMeta(players).getTeam() == Team.YELLOW) {
                yellowLore.add(String.valueOf(ChatColor.YELLOW + players.getName()));
            }
        }

        yellowMeta.setLore(yellowLore);
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
        ItemStack clickedItem = e.getCurrentItem();

         if (e.getClickedInventory() instanceof PlayerInventory && !player.getWorld().getName().equalsIgnoreCase("lobby")) {
             e.setCancelled(false);
         } else {
             e.setCancelled(true);
         }

         if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

         if (e.getInventory().getName().equalsIgnoreCase("Team Selector")) {
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
         } else if (e.getInventory().getName().equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Maps")) {
             FileConfiguration config = instance.getConfigManager().getConfig("maps.yml");
             if (config.contains(clickedItem.getItemMeta().getDisplayName())) {
                 Bukkit.dispatchCommand(player, "vote " + clickedItem.getItemMeta().getDisplayName());
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
            Menu shop = new ShopMenu(instance.getMapManager().getCurrentMap(), player, Category.BLOCKS);
            shop.open();
              System.out.println(player.getName() + " Interacted with villager");
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

        instance.getScoreboardHandler().setTeam(Team.BLUE);
        instance.getScoreboardHandler().setTeam(Team.RED);
        instance.getScoreboardHandler().setTeam(Team.GREEN);
        instance.getScoreboardHandler().setTeam(Team.YELLOW);
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
        } else {
            e.setCancelled(false);
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
