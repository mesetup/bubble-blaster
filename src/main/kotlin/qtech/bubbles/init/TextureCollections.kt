package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.common.mods.ModInstance
import qtech.bubbles.event.bus.LocalAddonEventBus
import qtech.hydro.TextureCollection
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

/**
 * Initialization for texture collections.
 *
 * @see Registers.TEXTURE_COLLECTIONS
 *
 * @since 1.0.924-a1
 */
object TextureCollections {
    private val TEXTURE_COLLECTIONS: DeferredRegister<TextureCollection> = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.TEXTURE_COLLECTIONS)
    @JvmField
    val BUBBLE_TEXTURES: RegistryObject<TextureCollection> = register("bubble") { TextureCollection() }
    private fun register(name: String, supplier: Supplier<TextureCollection>): RegistryObject<TextureCollection> {
        return TEXTURE_COLLECTIONS.register(name, supplier)
    }

    @JvmStatic
    fun register(eventBus: LocalAddonEventBus<out ModInstance>) {
        TEXTURE_COLLECTIONS.register(eventBus)
    }
}