package com.qtech.bubbles.event;

import com.qtech.bubbles.addon.loader.AddonLoader;

public class LoaderGameSetupEvent extends Event {
    private final AddonLoader loader;

    public LoaderGameSetupEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}