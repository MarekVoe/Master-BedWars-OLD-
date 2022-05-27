package me.mastergamercz.bedwars;

import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.Location;

public class TeamBed {

    private Team team;
    private Location location;
    private boolean alive = true;

    public TeamBed(Team team, Location location) {
        this.team = team;
        this.location = location;
    }

    public Team getTeam() {
        return team;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
