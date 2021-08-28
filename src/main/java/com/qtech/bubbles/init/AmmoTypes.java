package com.qtech.bubbles.init;

import com.qtech.bubbles.InternalAddon;
import com.qtech.bubbles.common.init.ObjectInit;
import com.qtech.bubbles.entity.ammo.AmmoType;
import com.qtech.bubbles.entity.ammo.BasicAmmoType;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * @see AmmoType
 */
public class AmmoTypes implements ObjectInit<AmmoType> {
    public static final DeferredRegister<AmmoType> AMMO_TYPES = DeferredRegister.create(InternalAddon.ADDON_ID, Registers.AMMO_TYPES);

    public static final RegistryObject<BasicAmmoType> BASIC = register("basic", BasicAmmoType::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends AmmoType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return AMMO_TYPES.register(name, supplier);
    }
}
