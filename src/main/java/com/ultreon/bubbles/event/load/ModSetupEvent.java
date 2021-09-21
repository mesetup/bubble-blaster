package com.ultreon.bubbles.event.load;

import com.ultreon.bubbles.mod.loader.ModLoader;
import com.ultreon.hydro.event.Event;

/**
 * <h1>Mod Setup Event</h1>
 * This event is for post-initialize mods.
 *
 * @since 1.0.0
 */
public class ModSetupEvent extends Event {
    private final ModLoader loader;

    public ModSetupEvent(ModLoader loader) {
        this.loader = loader;
    }

    public ModLoader getLoader() {
        return loader;
    }
}
