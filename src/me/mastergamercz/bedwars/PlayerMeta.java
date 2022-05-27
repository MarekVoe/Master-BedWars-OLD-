package me.mastergamercz.bedwars;

import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerMeta {
    private static HashMap<String, PlayerMeta> metaTable = new HashMap<String, PlayerMeta>();
    private int deaths;
    private int kills;
    private int beds_destroyed;
    private int wins;
    private int losses;

    public static PlayerMeta getMeta(Player player) {
        return getMeta(player.getName());
    }

    public static PlayerMeta getMeta(String username) {
        if (!metaTable.containsKey(username))
            metaTable.put(username, new PlayerMeta());
        return metaTable.get(username);
    }

    public static void reset() {
        metaTable.clear();
    }

    private Team team;
    private boolean alive;

    public PlayerMeta() {
        team = Team.NONE;
        alive = false;
    }

    public void setTeam(Team t) {
        if (team != null)
            team = t;
        else
            team = Team.NONE;
    }

    public Team getTeam() {
        return team;
    }

    public void setAlive(boolean b) {
        alive = b;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setBeds_destroyed(int beds_destroyed) {
        this.beds_destroyed = beds_destroyed;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getBeds_destroyed() {
        return beds_destroyed;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

}

