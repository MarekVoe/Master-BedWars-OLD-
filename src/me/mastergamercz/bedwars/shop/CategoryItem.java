package me.mastergamercz.bedwars.shop;

import me.mastergamercz.bedwars.UI.MenuItem;
import me.mastergamercz.bedwars.maps.GameMap;
import org.bukkit.entity.Player;

public class CategoryItem extends MenuItem {
    public CategoryItem(GameMap gameMap, Category category, Player player) {
        super(category.getItem());
        this.addClickAction((event ->  new ShopMenu(gameMap, player, category).open()));
    }
}
