package com.qtech.bubbles.event.bus;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.event.Event;
import com.qtech.bubbles.event._common.ICancellable;

public class AddonEventBus extends EventBus {
    @Override
    public <T extends Event> boolean post(T event) {
        BubbleBlaster.getInstance().getAddonManager().getContainers().stream().map(AddonContainer::getAddonObject).forEach((addonObject) -> {
            BubbleBlaster.getLogger().info("Sending addon event to: " + addonObject.getNamespace());
            addonObject.getEventBus().post(event);
        });

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }
}
