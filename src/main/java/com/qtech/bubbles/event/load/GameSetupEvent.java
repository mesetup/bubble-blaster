package com.qtech.bubbles.event.load;

import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.event.Event;

public class GameSetupEvent extends Event {
    private final AddonLoader loader;

    public GameSetupEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}