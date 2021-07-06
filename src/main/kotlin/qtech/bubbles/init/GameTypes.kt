package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.common.gametype.AbstractGameMode
import qtech.bubbles.common.init.ObjectInit
import qtech.bubbles.gamemode.ClassicMode
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

/**
 * @author Quinten Jungblut
 * @see AbstractGameMode
 *
 * @since 1.0.0
 */
//@ObjectHolder(addonId = "qbubbles", type = GameType.class)
object GameTypes : ObjectInit<AbstractGameMode?> {
    val GAME_TYPES: DeferredRegister<AbstractGameMode> = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.GAME_TYPES)
    val CLASSIC_MODE: RegistryObject<ClassicMode> = register("classic") { ClassicMode() }
    private fun <T : AbstractGameMode> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return GAME_TYPES.register(name, supplier)
    }
}