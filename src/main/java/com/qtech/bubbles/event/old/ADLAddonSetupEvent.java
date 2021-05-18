package com.qtech.bubbles.event.old;

import com.google.common.eventbus.EventBus;
import com.qtech.bubbles.addon.loader.AddonLoader;
import com.qtech.bubbles.event.Event;

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
