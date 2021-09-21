package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalMod;
import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.gametype.ClassicType;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.hydro.registry.ObjectInit;
import com.ultreon.hydro.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * @author Qboi
 * @see AbstractGameType
 * @since 1.0.0
 */
@SuppressWarnings("unused")
//@ObjectHolder(modId = "bubbleblaster", type = GameType.class)
public class GameTypes implements ObjectInit<AbstractGameType> {
    public static final DeferredRegister<AbstractGameType> GAME_TYPES = DeferredRegister.create(InternalMod.MOD_ID, Registers.GAME_TYPES);

    public static final RegistryObject<ClassicType> CLASSIC_TYPE = register("classic", () -> new ClassicType());

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractGameType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_TYPES.register(name, supplier);
    }
}
