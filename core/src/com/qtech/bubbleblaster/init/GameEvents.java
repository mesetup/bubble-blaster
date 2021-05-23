package com.qtech.bubbleblaster.init;

import com.qtech.bubbleblaster.QInternalAddon;
import com.qtech.bubbleblaster.common.gamestate.GameEvent;
import com.qtech.bubbleblaster.common.init.ObjectInit;
import com.qtech.bubbleblaster.registry.DeferredRegister;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.registry.object.RegistryObject;
import com.qtech.bubbleblaster.state.BloodMoonEvent;

import java.util.function.Supplier;

/**
 * @see GameEvent
 */
@SuppressWarnings("unused")
public class GameEvents implements ObjectInit<GameEvent> {
    public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.GAME_EVENTS);

    // Bubbles
    public static final RegistryObject<BloodMoonEvent> BLOOD_MOON_EVENT = register("blood_moon", BloodMoonEvent::new);

    private static <T extends GameEvent> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return GAME_EVENTS.register(name, supplier);
    }
}
