package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalAddon;
import com.ultreon.hydro.registry.ObjectInit;
import com.ultreon.bubbles.entity.ammo.AmmoType;
import com.ultreon.bubbles.entity.ammo.BasicAmmoType;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.registry.object.RegistryObject;

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
