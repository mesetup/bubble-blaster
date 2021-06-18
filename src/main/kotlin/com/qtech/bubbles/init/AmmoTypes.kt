package com.qtech.bubbles.init

import com.qtech.bubbles.BBInternalAddon
import com.qtech.bubbles.common.ammo.AmmoType
import com.qtech.bubbles.common.ammo.BasicAmmoType
import com.qtech.bubbles.common.init.ObjectInit
import com.qtech.bubbles.registry.DeferredRegister
import com.qtech.bubbles.registry.Registers
import com.qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

/**
 * @see AmmoType
 */
object AmmoTypes : ObjectInit<AmmoType?> {
    val AMMO_TYPES: DeferredRegister<AmmoType> = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.AMMO_TYPES)
    val BASIC: RegistryObject<BasicAmmoType> = register("basic") { BasicAmmoType() }
    private fun <T : AmmoType> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return AMMO_TYPES.register(name, supplier)
    }
}