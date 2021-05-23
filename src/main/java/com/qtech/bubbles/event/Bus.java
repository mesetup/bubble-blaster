package com.qtech.bubbles.event;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.common.addon.AddonObject;
import com.qtech.bubbles.common.addon.QBubblesAddon;
import com.qtech.bubbles.event.bus.AddonEventBus;
import com.qtech.bubbles.event.bus.LocalAddonEventBus;
import com.qtech.bubbles.event.bus.QBubblesEventBus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Bus {
    private static final AddonEventBus addonEventBus = new AddonEventBus();

    public static LocalAddonEventBus<?> getLocalAddonEventBus(String addonId) {
        AddonContainer container = QBubbles.getInstance().getAddonManager().getContainerFromId(addonId);
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
        return QBubbles.getEventBus();
    }
}
