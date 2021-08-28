package com.qtech.bubbles.init;

import com.qtech.bubbles.InternalAddon;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.common.init.ObjectInit;
import com.qtech.bubbles.gametype.ClassicType;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * @author Quinten Jungblut
 * @see AbstractGameType
 * @since 1.0.0
 */
@SuppressWarnings("unused")
//@ObjectHolder(addonId = "qbubbles", type = GameType.class)
public class GameTypes implements ObjectInit<AbstractGameType> {
    public static final DeferredRegister<AbstractGameType> GAME_TYPES = DeferredRegister.create(InternalAddon.ADDON_ID, Registers.GAME_TYPES);

    public static final RegistryObject<ClassicType> CLASSIC_TYPE = register("classic", () -> new ClassicType());

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractGameType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_TYPES.register(name, supplier);
    }
}
