package com.qtech.bubbles.event.load;

import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.event.Event;

public class LoadCompleteEvent extends Event {
    private final AddonLoader loader;

    public LoadCompleteEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}