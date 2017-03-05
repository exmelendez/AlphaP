package com.example.android.alphap.Yuri;

public class GameSettings {
    private String name;
    private int players;
    private boolean timeVisibility;

    public GameSettings(String name, int players, boolean timeVisibility) {
        this.name = name;
        this.players = players;
        this.timeVisibility = timeVisibility;
    }

    public String getName() {
        return name;
    }

    public int getPlayers() {
        return players;
    }

    public boolean isTimeVisibility() {
        return timeVisibility;
    }
}


