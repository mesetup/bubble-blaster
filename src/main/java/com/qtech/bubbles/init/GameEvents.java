package com.qtech.bubbles.init;

import com.qtech.bubbles.BBInternalAddon;
import com.qtech.bubbles.common.gamestate.GameEvent;
import com.qtech.bubbles.common.init.ObjectInit;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;
import com.qtech.bubbles.state.BloodMoonEvent;

import java.util.function.Supplier;

/**
 * @see GameEvent
 */
@SuppressWarnings("unused")
public class GameEvents implements ObjectInit<GameEvent> {
    public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.GAME_EVENTS);

    // Bubbles
    public static final RegistryObject<BloodMoonEvent> BLOOD_MOON_EVENT = register("blood_moon", BloodMoonEvent::new);

    private static <T extends GameEvent> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_EVENTS.register(name, supplier);
    }
}
