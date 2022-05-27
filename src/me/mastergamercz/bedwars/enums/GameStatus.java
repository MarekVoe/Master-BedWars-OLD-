package me.mastergamercz.bedwars.enums;

import org.bukkit.Color;

public enum GameStatus {
    LOBBY,INGAME;

    private Color color;


    public Color getColor() {
        switch(this) {
            case LOBBY:
                return Color.GREEN;
            case INGAME:
                return Color.RED;
        }
        return Color.WHITE;
    }


}
