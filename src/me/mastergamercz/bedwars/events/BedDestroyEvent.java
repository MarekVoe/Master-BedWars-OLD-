package me.mastergamercz.bedwars.events;


import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BedDestroyEvent extends Event {

    private Player player;
    private Team team;

    public BedDestroyEvent(Player player, Team team) {
          this.player = player;
          this.team = team;
    }

    private static final HandlerList handlers = new HandlerList();


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }
}
