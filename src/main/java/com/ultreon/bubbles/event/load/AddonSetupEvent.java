package com.ultreon.bubbles.event.load;

import com.ultreon.bubbles.mod.loader.ModLoader;
import com.ultreon.hydro.event.Event;

/**
 * <h1>Addon Setup Event</h1>
 * This event is for post-initialize addons.
 *
 * @since 1.0.0
 */
public class AddonSetupEvent extends Event {
    private final ModLoader loader;

    public AddonSetupEvent(ModLoader loader) {
        this.loader = loader;
    }

    public ModLoader getLoader() {
        return loader;
    }
}
