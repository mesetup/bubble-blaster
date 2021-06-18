package com.qtech.bubbles.registry.`object`

import com.qtech.bubbles.common.IRegistryEntry
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.registry.Registry
import java.util.function.Supplier

class RegistryObject<B : IRegistryEntry>(private val registry: Registry<in B>, private val supplier: Supplier<B>, private val resourceLocation: ResourceLocation) {
    fun register() {
        registry.register(resourceLocation, supplier.get())
    }

    @Suppress("UNCHECKED_CAST")
    fun get(): B {
        return registry[resourceLocation] as B
    }
}