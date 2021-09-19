package com.ultreon.hydro.event;

import com.ultreon.commons.lang.ICancellable;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.GameObject;

public class CollisionEvent extends Event implements ICancellable {
    private final Game game;
    private final GameObject source;
    private final GameObject target;

    public CollisionEvent(Game game, GameObject source, GameObject target) {
        this.game = game;
        this.source = source;
        this.target = target;
    }

    public Game getGame() {
        return game;
    }

    public GameObject getSource() {
        return source;
    }

    public GameObject getTarget() {
        return target;
    }
}
