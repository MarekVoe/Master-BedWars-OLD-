package me.mastergamercz.bedwars.managers;

import me.mastergamercz.bedwars.PlayerMeta;
import me.mastergamercz.bedwars.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class SoundManager {
    private static Random rand = new Random();

    public void playSound(Location loc, Sound sound, float volume, float minPitch, float maxPitch) {
        loc.getWorld().playSound(loc, sound, volume, randomPitch(minPitch, maxPitch));
    }

    public void playSoundForPlayer(Player p, Sound sound, float volume, float minPitch, float maxPitch) {
        p.playSound(p.getLocation(), sound, volume, randomPitch(minPitch, maxPitch));
    }

    public void playSoundForTeam(Team team, Sound sound, float volume, float minPitch, float maxPitch) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (PlayerMeta.getMeta(p).getTeam() == team)
                playSoundForPlayer(p, sound, volume, minPitch, maxPitch);
    }

    private float randomPitch(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }
}

