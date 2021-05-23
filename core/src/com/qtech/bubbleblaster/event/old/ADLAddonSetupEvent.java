package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.addon.loader.AddonLoader;
import com.qtech.bubbleblaster.event.Event;

/**
 * <h1>Addon Post-initialization Event</h1>
 * This event is for post-initialize addons.
 *
 * @since 1.0.0
 */
public class ADLAddonSetupEvent extends Event {
    private final AddonLoader loader;

    public ADLAddonSetupEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}
