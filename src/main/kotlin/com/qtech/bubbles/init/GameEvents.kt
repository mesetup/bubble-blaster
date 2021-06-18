package com.qtech.bubbles.init

import com.qtech.bubbles.BBInternalAddon
import com.qtech.bubbles.common.gamestate.GameEvent
import com.qtech.bubbles.common.init.ObjectInit
import com.qtech.bubbles.registry.DeferredRegister
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.registry.`object`.RegistryObject
import com.qtech.bubbles.state.BloodMoonEvent
import java.util.function.Supplier

/**
 * @see GameEvent
 */
object GameEvents : ObjectInit<GameEvent> {
    @JvmField
    val GAME_EVENTS: DeferredRegister<GameEvent> = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.GAME_EVENTS)

    // Bubbles
    val BLOOD_MOON_EVENT: RegistryObject<BloodMoonEvent> = register("blood_moon") { BloodMoonEvent() }

    @Suppress("SameParameterValue")
    private fun <T : GameEvent> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return GAME_EVENTS.register(name, supplier)
    }
}