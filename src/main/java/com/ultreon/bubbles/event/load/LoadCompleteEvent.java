package com.ultreon.bubbles.event.load;

import com.ultreon.bubbles.mod.loader.ModLoader;
import com.ultreon.hydro.event.Event;

public class LoadCompleteEvent extends Event {
    private final ModLoader loader;

    public LoadCompleteEvent(ModLoader loader) {
        this.loader = loader;
    }

    public ModLoader getLoader() {
        return loader;
    }
}