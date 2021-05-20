package com.qtech.bubbleblaster.init;

import com.qtech.bubbleblaster.QInternalAddon;
import com.qtech.bubbleblaster.common.addon.QBubblesAddon;
import com.qtech.bubbleblaster.event.bus.LocalAddonEventBus;
import com.qtech.bubbleblaster.graphics.TextureCollection;
import com.qtech.bubbleblaster.registry.DeferredRegister;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * Initialization for texture collections.
 *
 * @see Registers#TEXTURE_COLLECTIONS
 * @since 1.0.924-a1
 */
public class TextureCollections {
    private static final DeferredRegister<TextureCollection> TEXTURE_COLLECTIONS = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.TEXTURE_COLLECTIONS);

    public static final RegistryObject<TextureCollection> BUBBLE_TEXTURES = register("bubble", TextureCollection::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends TextureCollection> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return TEXTURE_COLLECTIONS.register(name, supplier);
    }

    public static void register(LocalAddonEventBus<? extends QBubblesAddon> eventBus) {
        TEXTURE_COLLECTIONS.register(eventBus);
    }
}
