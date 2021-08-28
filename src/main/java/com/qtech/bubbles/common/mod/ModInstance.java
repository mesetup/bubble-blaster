package com.qtech.bubbles.common.mod;

import com.qtech.bubbles.event.bus.LocalAddonEventBus;
import org.apache.logging.log4j.Logger;

public abstract class ModInstance {
    public final Logger logger;
    private final String addonId;
    private final ModObject<? extends ModInstance> modObject;

    public ModInstance(Logger logger, String addonId, ModObject<? extends ModInstance> modObject) {
        this.logger = logger;
        this.addonId = addonId;
        this.modObject = modObject;
    }

    // * See comment on cast
    public LocalAddonEventBus<? extends ModInstance> getEventBus() {
        return modObject.getEventBus();
    }

    public Logger getLogger() {
        return logger;
    }

    public String getAddonId() {
        return addonId;
    }

    public ModObject<? extends ModInstance> getAddonObject() {
        return modObject;
    }
}
