package com.ultreon.hydro.registry.object;

import com.ultreon.hydro.common.IRegistryEntry;
import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.registry.Registry;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RegistryObject<B extends IRegistryEntry> {
    private final Registry registry;
    private final Supplier<B> supplier;
    private final ResourceEntry resourceEntry;

    public <T extends B> RegistryObject(Registry<?> registry, Supplier<B> supplier, ResourceEntry resourceEntry) {
        this.registry = registry;
        this.supplier = supplier;
        this.resourceEntry = resourceEntry;
    }

    public void register() {
        registry.register(resourceEntry, (RegistryEntry) ((Supplier) supplier).get());
    }

    @SuppressWarnings("unchecked")
    public B get() {
        return (B) registry.get(resourceEntry);
    }
}
