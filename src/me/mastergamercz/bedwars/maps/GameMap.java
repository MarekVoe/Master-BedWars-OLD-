package me.mastergamercz.bedwars.maps;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;


public class GameMap {

    private World world;
    private MapRollback mapRollback;




    public GameMap(MapRollback mapRollback) {
        this.mapRollback = mapRollback;
    }

    public boolean loadIntoGame(String worldName) {
        mapRollback.loadMap(worldName);

        WorldCreator wc = new WorldCreator(worldName);
        wc.generator(new VoidGenerator());
        world = Bukkit.createWorld(wc);

        return true;
    }

    public String getName() {
        return world.getName();
    }
}
