package com.qsoftware.bubbles.event.old;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.addon.loader.AddonLoader;
import com.qsoftware.bubbles.event.Event;

/**
 * <h1>Addon Post-initialization Event</h1>
 * This event is for post-initialize addons.
 *
 * @since 1.0.0
 * @deprecated
 */
@Deprecated
public class ADLPostInitializationEvent extends Event {
    private static EventBus eventBus;
    private final AddonLoader loader;

    public static EventBus getEventBus() {
        return eventBus;
    }

    public ADLPostInitializationEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}
