package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.addon.loader.AddonLoader;

public class LoaderLoadCompleteEvent extends Event {
    private final AddonLoader loader;

    public LoaderLoadCompleteEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}