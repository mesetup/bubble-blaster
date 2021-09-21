package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalMod;
import com.ultreon.bubbles.common.gamestate.GameEvent;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.bubbles.state.BloodMoonEvent;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.hydro.registry.ObjectInit;
import com.ultreon.hydro.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * @see GameEvent
 */
@SuppressWarnings("unused")
public class GameEvents implements ObjectInit<GameEvent> {
    public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(InternalMod.MOD_ID, Registers.GAME_EVENTS);

    // Bubbles
    public static final RegistryObject<BloodMoonEvent> BLOOD_MOON_EVENT = register("blood_moon", BloodMoonEvent::new);

    private static <T extends GameEvent> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_EVENTS.register(name, supplier);
    }
}
