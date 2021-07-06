package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.ability.TeleportAbility
import qtech.bubbles.common.ability.AbilityType
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

object Abilities {
    val ABILITY_TYPES: DeferredRegister<AbilityType<*>> = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.ABILITIES)
    val TELEPORT_ABILITY: RegistryObject<AbilityType<TeleportAbility>> = register("teleport") { AbilityType { TeleportAbility() } }
    private fun <T : AbilityType<*>> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return ABILITY_TYPES.register(name, supplier)
    }
}