package me.mastergamercz.bedwars.chat;

import me.mastergamercz.bedwars.Main;
import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
           Player sender = e.getPlayer();
           PlayerMeta meta = PlayerMeta.getMeta(sender);
           Team team = meta.getTeam();
           boolean isAll = false;
           String msg = e.getMessage();

           if (e.getMessage().startsWith("!") && !e.getMessage().equalsIgnoreCase("!")) {
               isAll = true;
               msg = msg.substring(1);
           }

           if (team == Team.NONE) {
               isAll = true;
           }

           if (isAll)
               ChatUtil.allMessage(team, sender , msg);
           else
               ChatUtil.teamMessage(team, sender, msg);

           e.setCancelled(true);
    }
}
