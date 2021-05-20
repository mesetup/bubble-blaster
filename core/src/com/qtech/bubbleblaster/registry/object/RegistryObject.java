package com.qtech.bubbleblaster.registry.object;

import com.qtech.bubbleblaster.common.IRegistryEntry;
import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.ResourceLocation;
import com.qtech.bubbleblaster.registry.Registry;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RegistryObject<B extends IRegistryEntry> {
    private final Registry registry;
    private final Supplier<B> supplier;
    private final ResourceLocation resourceLocation;

    public <T extends B> RegistryObject(Registry registry, Supplier<B> supplier, ResourceLocation resourceLocation) {
        this.registry = registry;
        this.supplier = supplier;
        this.resourceLocation = resourceLocation;
    }

    public void register() {
        registry.register(resourceLocation, (RegistryEntry) ((Supplier) supplier).get());
    }

    @SuppressWarnings("unchecked")
    public B get() {
        return (B) registry.get(resourceLocation);
    }
}
