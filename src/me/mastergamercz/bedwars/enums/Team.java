package me.mastergamercz.bedwars.enums;

import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.TeamBed;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Team {
    RED,YELLOW,BLUE,GREEN,NONE;

    private final ChatColor color;
    private List<Location> spawns;
    private TeamBed teamBed;

    Team() {
        if (name().equals("NONE"))
            color = ChatColor.WHITE;
        else
            color = ChatColor.valueOf(name());

        spawns = new ArrayList<Location>();
    }

    @Override
    public String toString() {
        return name().substring(0, 1) + name().substring(1).toLowerCase();
    }

    public String coloredName() {
        return getColor().toString() + toString();
    }

    public ChatColor getColor() {
        return color;
    }

    public void addSpawn(Location location) {
        if (this != NONE)
            spawns.add(location);
    }

    public Location getRandomSpawn() {
        if (!spawns.isEmpty() && this != NONE)
            return spawns.get(new Random().nextInt(spawns.size()));
        return null;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<Player>();
        for (Player p : Bukkit.getOnlinePlayers())
            if (PlayerMeta.getMeta(p).getTeam() == this && this != NONE)
            players.add(p);
            return players;
    }

    public static Team[] teams() {
        return new Team[] {RED, YELLOW, GREEN, BLUE};
    }

    public boolean isFull() {
        if (getPlayers().size() == 4) {
            return true;
        } else if (getPlayers().size() < 4) {
            return false;
        }
        return false;
    }

    public Color getColor(Team team) {
        switch (team) {
            case GREEN:
                return Color.GREEN;
            case BLUE:
                return Color.BLUE;
            case YELLOW:
                return Color.YELLOW;
            case RED:
                return Color.RED;
        }
        return null;
    }

    public void loadTeamBed(Location location) {
        if (this != NONE)
            teamBed = new TeamBed(this, location);
    }

    public TeamBed getTeamBed() {
        return teamBed;
    }

    public void setSpawns(List<Location> spawns) {
        this.spawns = spawns;
    }

    public void setTeamBed(TeamBed teamBed) {
        this.teamBed = teamBed;
    }

    public ChatColor color() {
        return color;
    }
}
