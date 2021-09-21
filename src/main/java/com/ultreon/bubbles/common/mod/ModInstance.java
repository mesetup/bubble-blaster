package com.ultreon.bubbles.common.mod;

import com.ultreon.bubbles.event.bus.ModEvents;
import org.apache.logging.log4j.Logger;

public abstract class ModInstance {
    public final Logger logger;
    private final String modId;
    private final ModObject<? extends ModInstance> modObject;

    public ModInstance(Logger logger, String modId, ModObject<? extends ModInstance> modObject) {
        this.logger = logger;
        this.modId = modId;
        this.modObject = modObject;
    }

    // * See comment on cast
    public ModEvents<? extends ModInstance> getEventBus() {
        return modObject.getEventBus();
    }

    public Logger getLogger() {
        return logger;
    }

    public String getModId() {
        return modId;
    }

    public ModObject<? extends ModInstance> getModObject() {
        return modObject;
    }
}
