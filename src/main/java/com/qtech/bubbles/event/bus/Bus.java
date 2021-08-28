package com.qtech.bubbles.event.bus;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.addon.loader.AddonContainer;
import com.qtech.bubbles.common.mod.ModInstance;
import com.qtech.bubbles.common.mod.ModObject;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Bus {
    private static final AddonEventBus addonEventBus = new AddonEventBus();

    public static LocalAddonEventBus<?> getLocalAddonEventBus(String addonId) {
        AddonContainer container = BubbleBlaster.getInstance().getAddonManager().getContainerFromId(addonId);
        ModObject<? extends ModInstance> modObject;
        if (container != null) {
            modObject = container.getAddonObject();
            return modObject.getEventBus();
        }
        throw new NullPointerException();
    }

    public static @NotNull LocalAddonEventBus<? extends ModInstance> getLocalAddonEventBus(ModInstance addon) {
        ModObject<? extends ModInstance> modObject = addon.getAddonObject();
        return modObject.getEventBus();
    }

    public static <T extends ModInstance> LocalAddonEventBus<T> getLocalAddonEventBus(ModObject<T> modObject) {
        return modObject.getEventBus();
    }

    public static AddonEventBus getAddonEventBus() {
        return addonEventBus;
    }

    public static @NotNull GameEventBus getQBubblesEventBus() {
        return BubbleBlaster.getEventBus();
    }
}
