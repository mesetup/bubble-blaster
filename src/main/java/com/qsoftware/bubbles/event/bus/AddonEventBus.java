package com.qsoftware.bubbles.event.bus;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.addon.loader.AddonContainer;
import com.qsoftware.bubbles.event.Event;
import com.qsoftware.bubbles.event.ICancellable;

public class AddonEventBus extends EventBus {
    @Override
    public <T extends Event> boolean post(T event) {
        QBubbles.getInstance().getAddonManager().getContainers().stream().map(AddonContainer::getAddonObject).forEach((addonObject) -> {
            QBubbles.getLogger().info("Sending addon event to: " + addonObject.getNamespace());
            addonObject.getEventBus().post(event);
        });

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }
}
