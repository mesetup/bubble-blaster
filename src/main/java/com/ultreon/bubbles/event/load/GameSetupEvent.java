package com.ultreon.bubbles.event.load;

import com.ultreon.bubbles.mod.loader.ModLoader;
import com.ultreon.hydro.event.Event;

public class GameSetupEvent extends Event {
    private final ModLoader loader;

    public GameSetupEvent(ModLoader loader) {
        this.loader = loader;
    }

    public ModLoader getLoader() {
        return loader;
    }
}