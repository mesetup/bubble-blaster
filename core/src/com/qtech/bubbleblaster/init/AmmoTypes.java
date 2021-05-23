package com.qtech.bubbleblaster.init;

import com.qtech.bubbleblaster.QInternalAddon;
import com.qtech.bubbleblaster.common.ammo.AmmoType;
import com.qtech.bubbleblaster.common.ammo.BasicAmmoType;
import com.qtech.bubbleblaster.common.init.ObjectInit;
import com.qtech.bubbleblaster.registry.DeferredRegister;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * @see AmmoType
 */
public class AmmoTypes implements ObjectInit<AmmoType> {
    public static final DeferredRegister<AmmoType> AMMO_TYPES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.AMMO_TYPES);

    public static final RegistryObject<BasicAmmoType> BASIC = register("basic", BasicAmmoType::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends AmmoType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return AMMO_TYPES.register(name, supplier);
    }
}
