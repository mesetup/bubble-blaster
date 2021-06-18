package com.qtech.bubbles.registry

import com.qtech.bubbles.common.IRegistryEntry
import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.event.SubscribeEvent
import com.qtech.bubbles.event.bus.LocalAddonEventBus
import com.qtech.bubbles.event.registry.RegistryEvent
import com.qtech.bubbles.registry.`object`.RegistryObject
import java.util.AbstractMap.SimpleEntry
import java.util.function.Supplier

class DeferredRegister<T : IRegistryEntry> private constructor(private val addonId: String?, private val registry: Registry<T>) {
    private val objects = ArrayList<Map.Entry<ResourceLocation, Supplier<T>>>()
    fun <C : T> register(key: String, supplier: Supplier<C>): RegistryObject<C> {
        val rl = ResourceLocation(addonId, key)

//        if (!registry.getType().isAssignableFrom(supplier.get().getClass())) {
//            throw new IllegalArgumentException("Tried to register illegal type: " + supplier.get().getClass() + " expected assignable to " + registry.getType());
//        }
        objects.add(SimpleEntry(rl, Supplier { supplier.get() }))
        return RegistryObject(registry, supplier, rl)
    }

    fun register(eventBus: LocalAddonEventBus<out AddonInstance>) {
        eventBus.register(this)
        eventBus.addListener(::onRegister)
    }

    @SubscribeEvent
    @JvmName("onRegister")
    fun onRegister(event: RegistryEvent.Register<T>) {
        if (event.registry.type != registry.type) {
            return
        }

        println("Deferred Register dump: " + event.registry.type);
        for ((rl, value) in objects) {
            val `object` = value.get()
            require(event.registry.type.isAssignableFrom(`object`.javaClass)) { "Got invalid type in deferred register: " + `object`.javaClass + " expected assignable to " + event.registry.type }

            println("  ($rl) -> $`object`");
            event.registry.register(rl, `object`)
            `object`.registryName = rl
        }
    }

    companion object {
        fun <T : IRegistryEntry> create(addonId: String?, registry: Registry<T>): DeferredRegister<T> {
            return DeferredRegister(addonId, registry)
        }
    }
}