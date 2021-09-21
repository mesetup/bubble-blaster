package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalMod;
import com.ultreon.bubbles.common.mod.ModInstance;
import com.ultreon.bubbles.event.bus.ModEvents;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.event.bus.GameEvents;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.hydro.registry.object.RegistryObject;
import com.ultreon.hydro.render.TextureCollection;

import java.util.function.Supplier;

/**
 * Initialization for texture collections.
 *
 * @see Registers#TEXTURE_COLLECTIONS
 * @since 1.0.924-a1
 */
public class TextureCollections {
    private static final DeferredRegister<TextureCollection> TEXTURE_COLLECTIONS = DeferredRegister.create(InternalMod.MOD_ID, Registers.TEXTURE_COLLECTIONS);

    public static final RegistryObject<TextureCollection> BUBBLE_TEXTURES = register("bubble", TextureCollection::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends TextureCollection> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return TEXTURE_COLLECTIONS.register(name, supplier);
    }

    public static void register(GameEvents eventBus) {
        TEXTURE_COLLECTIONS.register(eventBus);
    }
}
