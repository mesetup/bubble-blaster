package com.ultreon.bubbles.event.bus;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.common.mod.ModObject;
import com.ultreon.bubbles.event.ModEvent;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.ModList;
import com.ultreon.commons.lang.ICancellable;
import com.ultreon.hydro.event.bus.AbstractEvents;
import org.apache.logging.log4j.LogManager;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModEvents<J extends ModInstance> extends AbstractEvents<ModEvent> {
    private final ModObject<J> modObject;

    @SuppressWarnings("unchecked")
    public ModEvents(J mod) {
        super(LogManager.getLogger("Mod-Events"));
        this.modObject = (ModObject<J>) mod.getModObject();
    }

    public ModEvents(ModObject<J> modObject) {
        super(LogManager.getLogger("Mod-Events"));
        this.modObject = modObject;
    }

    @Deprecated
    @SuppressWarnings("UnusedReturnValue")
    public static <T extends ModEvent> boolean publishAll(T event) {
        ModList.get().getContainers().stream().map(ModContainer::getModObject).forEach((modObject) -> {
            BubbleBlaster.getLogger().info("Sending mod event to: " + modObject.getNamespace());
            modObject.getEventBus().publish(event);
        });

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }

    public ModObject<J> getModObject() {
        return modObject;
    }

    public J getMod() {
        return modObject.getMod();
    }
}
