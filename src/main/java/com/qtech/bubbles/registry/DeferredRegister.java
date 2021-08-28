package com.qtech.bubbles.registry;

import com.qtech.bubbles.common.IRegistryEntry;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.mod.ModInstance;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.event.bus.LocalAddonEventBus;
import com.qtech.bubbles.event.registry.RegistryEvent;
import com.qtech.bubbles.registry.object.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class DeferredRegister<T extends IRegistryEntry> {
    private final String addonId;
    private final Registry<T> registry;
    private final ArrayList<HashMap.Entry<ResourceEntry, Supplier<T>>> objects = new ArrayList<>();

    protected DeferredRegister(String addonId, Registry<T> registry) {
        this.addonId = addonId;
        this.registry = registry;
    }

    public static <T extends RegistryEntry> DeferredRegister<T> create(String addonId, Registry<T> registry) {
        return new DeferredRegister<>(addonId, registry);
    }

    public <C extends T> RegistryObject<C> register(String key, Supplier<C> supplier) {
        ResourceEntry rl = new ResourceEntry(addonId, key);

//        if (!registry.getType().isAssignableFrom(supplier.get().getClass())) {
//            throw new IllegalArgumentException("Tried to register illegal type: " + supplier.get().getClass() + " expected assignable to " + registry.getType());
//        }

        objects.add(new HashMap.SimpleEntry<>(rl, supplier::get));

        return new RegistryObject<>(registry, supplier, rl);
    }

    public void register(LocalAddonEventBus<? extends ModInstance> eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onRegister(RegistryEvent.Register<T> event) {
        if (!event.getRegistry().getType().equals(registry.getType())) {
            return;
        }

//        System.out.println("Deferred Register dump: " + event.getRegistry().getType());
        for (HashMap.Entry<ResourceEntry, Supplier<T>> entry : objects) {
            T object = entry.getValue().get();
            ResourceEntry rl = entry.getKey();

            if (!event.getRegistry().getType().isAssignableFrom(object.getClass())) {
                throw new IllegalArgumentException("Got invalid type in deferred register: " + object.getClass() + " expected assignable to " + event.getRegistry().getType());
            }

//            System.out.println("  (" + rl + ") -> " + object);

            event.getRegistry().register(rl, object);
            object.setRegistryName(rl);
        }
    }
}
