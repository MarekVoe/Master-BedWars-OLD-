package me.mastergamercz.bedwars.managers;

import me.mastergamercz.bedwars.items.LuckyBlock;
import me.mastergamercz.bedwars.items.Mine;

public class ItemManager {
    private final LuckyBlock luckyBlock;
    private final Mine mine;

    public ItemManager() {
        this.luckyBlock = new LuckyBlock();
        this.mine = new Mine();
    }

    public LuckyBlock getLuckyBlock() {
        return luckyBlock;
    }

    public Mine getMine() {
        return mine;
    }
}
