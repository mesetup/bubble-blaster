package com.qtech.bubbles.init

import com.qtech.bubbles.BBInternalAddon
import com.qtech.bubbles.ability.TeleportAbility
import com.qtech.bubbles.common.ability.AbilityType
import com.qtech.bubbles.registry.DeferredRegister
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

object Abilities {
    val ABILITY_TYPES: DeferredRegister<AbilityType<*>> = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.ABILITIES)
    val TELEPORT_ABILITY: RegistryObject<AbilityType<TeleportAbility>> = register("teleport") { AbilityType { TeleportAbility() } }
    private fun <T : AbilityType<*>> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return ABILITY_TYPES.register(name, supplier)
    }
}