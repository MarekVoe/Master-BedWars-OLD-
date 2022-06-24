package me.mastergamercz.bedwars.enums;

import me.mastergamercz.bedwars.gens.Resource;
import org.bukkit.Material;

public enum GenType {
    BRONZE(1, Resource.BRONZE, Material.HARD_CLAY, false),
    IRON(10, Resource.IRON, Material.IRON_BLOCK, true),
    GOLD(20, Resource.GOLD, Material.GOLD_BLOCK, true);

    private final int interval;
    private final Resource resource;
    private final Material block;
    private final boolean hologram;

    GenType(int interval, Resource resource, Material block, boolean hologram) {
        this.interval = interval;
        this.resource = resource;
        this.block = block;
        this.hologram = hologram;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isHologram() {
        return hologram;
    }

    public Material getBlock() {
        return block;
    }

    public Resource getResource() {
        return resource;
    }
}
