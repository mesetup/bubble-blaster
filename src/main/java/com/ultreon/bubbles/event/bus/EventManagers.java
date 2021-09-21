package com.ultreon.bubbles.event.bus;

import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.ModList;
import com.ultreon.hydro.event.bus.GameEvents;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public final class EventManagers {
    public static ModEvents<?> getModEvents(String modId) {
        ModContainer container = ModList.get().getContainerById(modId);
        ModObject<? extends ModInstance> modObject;
        if (container != null) {
            modObject = container.getModObject();
            return modObject.getEventBus();
        }
        throw new NullPointerException();
    }

    public static @NotNull ModEvents<? extends ModInstance> getModEvents(ModInstance mod) {
        ModObject<? extends ModInstance> modObject = mod.getModObject();
        return modObject.getEventBus();
    }

    @SuppressWarnings("unused")
    public static <T extends ModInstance> ModEvents<T> getModEvents(ModObject<T> modObject) {
        return modObject.getEventBus();
    }

    /**
     * @return the game event manager instance.
     * @deprecated replaced by {@link GameEvents#get()}
     */
    @Deprecated
    public static @NotNull GameEvents getGameEvents() {
        return GameEvents.get();
    }
}
