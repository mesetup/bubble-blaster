package com.ultreon.bubbles.event.bus;

import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.ModList;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.hydro.event.bus.GameEventBus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Bus {
    private static final AddonEventBus addonEventBus = new AddonEventBus();

    public static LocalAddonEventBus<?> getLocalAddonEventBus(String addonId) {
        ModContainer container = ModList.get().getContainerById(addonId);
        ModObject<? extends ModInstance> modObject;
        if (container != null) {
            modObject = container.getModObject();
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
        return GameEventBus.get();
    }
}
