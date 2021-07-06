package qtech.bubbles.init

import qtech.bubbles.BBInternalMod
import qtech.bubbles.common.ammo.AmmoType
import qtech.bubbles.common.ammo.BasicAmmoType
import qtech.bubbles.common.init.ObjectInit
import qtech.bubbles.registry.DeferredRegister
import qtech.bubbles.registry.Registers
import qtech.bubbles.registry.`object`.RegistryObject
import java.util.function.Supplier

/**
 * @see AmmoType
 */
object AmmoTypes : ObjectInit<AmmoType?> {
    val AMMO_TYPES: DeferredRegister<AmmoType> = DeferredRegister.create(BBInternalMod.ADDON_ID, Registers.AMMO_TYPES)
    val BASIC: RegistryObject<BasicAmmoType> = register("basic") { BasicAmmoType() }
    private fun <T : AmmoType> register(name: String, supplier: Supplier<T>): RegistryObject<T> {
        return AMMO_TYPES.register(name, supplier)
    }
}