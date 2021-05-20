package com.qtech.bubbleblaster.init;

import com.qtech.bubbleblaster.QInternalAddon;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.common.init.ObjectInit;
import com.qtech.bubbleblaster.gametype.ClassicType;
import com.qtech.bubbleblaster.registry.DeferredRegister;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * @author Quinten Jungblut
 * @see AbstractGameType
 * @since 1.0.0
 */
@SuppressWarnings("unused")
//@ObjectHolder(addonId = "qbubbles", type = GameType.class)
public class GameTypes implements ObjectInit<AbstractGameType> {
    public static final DeferredRegister<AbstractGameType> GAME_TYPES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.GAME_TYPES);

    public static final RegistryObject<ClassicType> CLASSIC_TYPE = register("classic", () -> new ClassicType());

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractGameType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_TYPES.register(name, supplier);
    }
}
