package com.ultreon.bubbles.event.bus;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.mod.ModContainer;
import com.ultreon.bubbles.mod.ModList;
import com.ultreon.hydro.event.Event;
import com.ultreon.commons.lang.ICancellable;
import com.ultreon.hydro.event.bus.EventBus;

public class AddonEventBus extends EventBus {
    @Override
    public <T extends Event> boolean post(T event) {
        ModList.get().getContainers().stream().map(ModContainer::getModObject).forEach((addonObject) -> {
            BubbleBlaster.getLogger().info("Sending addon event to: " + addonObject.getNamespace());
            addonObject.getEventBus().post(event);
        });

        return event instanceof ICancellable && ((ICancellable) event).isCancelled();
    }

    @Override
    public void register(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void register(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(Class<? extends Event> event, Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(Class<? extends Event> event, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(Object o) {
        throw new UnsupportedOperationException();
    }
}
