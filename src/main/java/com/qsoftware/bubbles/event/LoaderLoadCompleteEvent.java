package com.qsoftware.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.addon.loader.AddonLoader;

public class LoaderLoadCompleteEvent extends Event {
    private static EventBus eventBus;
    private final AddonLoader loader;

    public static EventBus getEventBus() {
        return eventBus;
    }

    public LoaderLoadCompleteEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}