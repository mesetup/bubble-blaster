package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.addon.loader.AddonLoader;
import com.qtech.bubbleblaster.event.Event;

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
