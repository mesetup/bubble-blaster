package com.ultreon.hydro.event;

import com.ultreon.hydro.Game;

public class GameExitEvent extends Event {
    private final Game game;

    public GameExitEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
