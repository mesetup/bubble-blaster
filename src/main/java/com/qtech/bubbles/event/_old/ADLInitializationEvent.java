package com.qtech.bubbles.event._old;

import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.event.Event;

/**
 * <h1>Addon Initialization Event</h1>
 * This event is for initialize addons.
 *
 * @since 1.0.0
 * @deprecated
 */
@Deprecated
public class ADLInitializationEvent extends Event {
    private final AddonLoader loader;

    public ADLInitializationEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}
