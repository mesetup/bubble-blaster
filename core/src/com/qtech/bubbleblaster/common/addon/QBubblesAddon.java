package com.qtech.bubbleblaster.common.addon;

import com.qtech.bubbleblaster.event.bus.LocalAddonEventBus;
import org.apache.logging.log4j.Logger;

public abstract class QBubblesAddon {
    public final Logger logger;
    private final String addonId;
    private final AddonObject<? extends QBubblesAddon> addonObject;

    public QBubblesAddon(Logger logger, String addonId, AddonObject<? extends QBubblesAddon> addonObject) {
        this.logger = logger;
        this.addonId = addonId;
        this.addonObject = addonObject;
    }

    // * See comment on cast
    public LocalAddonEventBus<? extends QBubblesAddon> getEventBus() {
        return addonObject.getEventBus();
    }

    public Logger getLogger() {
        return logger;
    }

    public String getAddonId() {
        return addonId;
    }

    public AddonObject<? extends QBubblesAddon> getAddonObject() {
        return addonObject;
    }
}
