package com.qtech.bubbles.init;

import com.qtech.bubbles.BBInternalAddon;
import com.qtech.bubbles.common.addon.QBubblesAddon;
import com.qtech.bubbles.event.bus.LocalAddonEventBus;
import com.qtech.bubbles.graphics.TextureCollection;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * Initialization for texture collections.
 *
 * @see Registers#TEXTURE_COLLECTIONS
 * @since 1.0.924-a1
 */
public class TextureCollections {
    private static final DeferredRegister<TextureCollection> TEXTURE_COLLECTIONS = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.TEXTURE_COLLECTIONS);

    public static final RegistryObject<TextureCollection> BUBBLE_TEXTURES = register("bubble", TextureCollection::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends TextureCollection> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return TEXTURE_COLLECTIONS.register(name, supplier);
    }

    public static void register(LocalAddonEventBus<? extends QBubblesAddon> eventBus) {
        TEXTURE_COLLECTIONS.register(eventBus);
    }
}
