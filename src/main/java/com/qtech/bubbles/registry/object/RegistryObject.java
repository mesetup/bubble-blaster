package com.qtech.bubbles.registry.object;

import com.qtech.bubbles.common.IRegistryEntry;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.registry.Registry;
import com.qtech.bubbles.registry.RegistryEntry;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RegistryObject<B extends IRegistryEntry> {
    private final Registry registry;
    private final Supplier<B> supplier;
    private final ResourceEntry resourceEntry;

    public <T extends B> RegistryObject(Registry registry, Supplier<B> supplier, ResourceEntry resourceEntry) {
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
