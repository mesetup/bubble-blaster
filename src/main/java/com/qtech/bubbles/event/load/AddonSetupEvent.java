package com.qtech.bubbles.event.load;

import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.event.Event;

/**
 * <h1>Addon Setup Event</h1>
 * This event is for post-initialize addons.
 *
 * @since 1.0.0
 */
public class AddonSetupEvent extends Event {
    private final AddonLoader loader;

    public AddonSetupEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}
