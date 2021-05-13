package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.QInternalAddon;
import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.bubble.NormalBubble;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.init.ObjectInit;
import com.qsoftware.bubbles.gametype.ClassicType;
import com.qsoftware.bubbles.registry.DeferredRegister;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * <h1>Bubble Initialization</h1>
 * Bubble init, used for initialize bubbles.
 * For example, the {@link NormalBubble} instance is assigned here.
 *
 * @see AbstractBubble
 */
@SuppressWarnings("unused")
//@ObjectHolder(addonId = "qbubbles", type = GameType.class)
public class GameTypeInit implements ObjectInit<AbstractGameType> {
    public static final DeferredRegister<AbstractGameType> GAME_TYPES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.GAME_TYPES);

    public static final RegistryObject<ClassicType> CLASSIC_TYPE = register("classic", () -> new ClassicType());

    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractGameType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_TYPES.register(name, supplier);
    }
}
