package com.qtech.bubbles.event;

import com.qtech.bubbles.addon.loader.AddonLoader;

public class LoaderLoadCompleteEvent extends Event {
    private final AddonLoader loader;

    public LoaderLoadCompleteEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}