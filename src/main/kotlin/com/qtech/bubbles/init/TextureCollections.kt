package com.qtech.bubbles.init

import com.qtech.bubbles.BBInternalAddon
import com.qtech.bubbles.common.addon.AddonInstance
import com.qtech.bubbles.event.bus.LocalAddonEventBus
import com.qtech.bubbles.graphics.TextureCollection
import com.qtech.bubbles.registry.DeferredRegister
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

/**
 * Initialization for texture collections.
 *
 * @see Registers.TEXTURE_COLLECTIONS
 *
 * @since 1.0.924-a1
 */
object TextureCollections {
    private val TEXTURE_COLLECTIONS: DeferredRegister<TextureCollection> = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.TEXTURE_COLLECTIONS)
    @JvmField
    val BUBBLE_TEXTURES: RegistryObject<TextureCollection> = register("bubble") { TextureCollection() }
    private fun register(name: String, supplier: Supplier<TextureCollection>): RegistryObject<TextureCollection> {
        return TEXTURE_COLLECTIONS.register(name, supplier)
    }

    @JvmStatic
    fun register(eventBus: LocalAddonEventBus<out AddonInstance>) {
        TEXTURE_COLLECTIONS.register(eventBus)
    }
}