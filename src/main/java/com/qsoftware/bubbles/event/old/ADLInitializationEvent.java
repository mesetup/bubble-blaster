package com.qsoftware.bubbles.event.old;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.addon.loader.AddonLoader;
import com.qsoftware.bubbles.event.Event;

/**
 * <h1>Addon Initialization Event</h1>
 * This event is for initialize addons.
 *
 * @since 1.0.0
 * @deprecated
 */
@Deprecated
public class ADLInitializationEvent extends Event {
    private static EventBus eventBus;
    private final AddonLoader loader;

    public static EventBus getEventBus() {
        return eventBus;
    }

    public ADLInitializationEvent(AddonLoader loader) {
        this.loader = loader;
    }

    public AddonLoader getLoader() {
        return loader;
    }
}
