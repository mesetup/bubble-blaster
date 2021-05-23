package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.addon.loader.AddonLoader;
import com.qtech.bubbleblaster.event.Event;

/**
 * <h1>Addon Pre-initialization Event</h1>
 * This event is for pre-initialize addons.
 *
 * @since 1.0.0
 * @deprecated
 */
@Deprecated
public class ADLPreInitializationEvent extends Event {
    private final AddonLoader loader;

    public ADLPreInitializationEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}
