package com.qtech.bubbleblaster.event.bus;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.addon.loader.AddonContainer;
import com.qtech.bubbleblaster.event.Event;
import com.qtech.bubbleblaster.event.ICancellable;

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
