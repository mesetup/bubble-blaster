package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.addon.loader.AddonContainer;
import com.qtech.bubbleblaster.common.addon.AddonObject;
import com.qtech.bubbleblaster.common.addon.QBubblesAddon;
import com.qtech.bubbleblaster.event.bus.AddonEventBus;
import com.qtech.bubbleblaster.event.bus.LocalAddonEventBus;
import com.qtech.bubbleblaster.event.bus.QBubblesEventBus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Bus {
    private static final AddonEventBus addonEventBus = new AddonEventBus();

    public static LocalAddonEventBus<?> getLocalAddonEventBus(String addonId) {
        AddonContainer container = BubbleBlaster.getInstance().getAddonManager().getContainerFromId(addonId);
        AddonObject<? extends QBubblesAddon> addonObject;
        if (container != null) {
            addonObject = container.getAddonObject();
            return addonObject.getEventBus();
        }
        throw new NullPointerException();
    }

    public static @NotNull LocalAddonEventBus<? extends QBubblesAddon> getLocalAddonEventBus(QBubblesAddon addon) {
        AddonObject<? extends QBubblesAddon> addonObject = addon.getAddonObject();
        return addonObject.getEventBus();
    }

    public static <T extends QBubblesAddon> LocalAddonEventBus<T> getLocalAddonEventBus(AddonObject<T> addonObject) {
        return addonObject.getEventBus();
    }

    public static AddonEventBus getAddonEventBus() {
        return addonEventBus;
    }

    public static @NotNull QBubblesEventBus getQBubblesEventBus() {
        return BubbleBlaster.getEventBus();
    }
}
