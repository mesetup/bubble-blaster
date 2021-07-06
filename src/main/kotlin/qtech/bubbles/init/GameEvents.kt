package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.common.gamestate.GameEvent
import qtech.bubbles.common.init.ObjectInit
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import qtech.bubbles.state.BloodMoonEvent
import java.util.function.Supplier

/**
 * @see GameEvent
 */
object GameEvents : ObjectInit<GameEvent> {
    @JvmField
    val GAME_EVENTS: DeferredRegister<GameEvent> = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.GAME_EVENTS)

    // Bubbles
    val BLOOD_MOON_EVENT: RegistryObject<BloodMoonEvent> = register("blood_moon") { BloodMoonEvent() }

    @Suppress("SameParameterValue")
    private fun <T : GameEvent> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return GAME_EVENTS.register(name, supplier)
    }
}