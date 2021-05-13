package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.QInternalAddon;
import com.qsoftware.bubbles.bubble.AbstractBubble;
import com.qsoftware.bubbles.bubble.NormalBubble;
import com.qsoftware.bubbles.common.ammo.AmmoType;
import com.qsoftware.bubbles.common.ammo.BasicAmmoType;
import com.qsoftware.bubbles.common.init.ObjectInit;
import com.qsoftware.bubbles.registry.DeferredRegister;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

/**
 * <h1>Bubble Initialization</h1>
 * Bubble init, used for initialize bubbles.
 * For example, the {@link NormalBubble} instance is assigned here.
 *
 * @see AbstractBubble
 */
//@ObjectHolder(addonId = "qbubbles")
public class AmmoInit implements ObjectInit<AmmoType> {
    public static final DeferredRegister<AmmoType> AMMO_TYPES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.AMMO_TYPES);

    public static final RegistryObject<BasicAmmoType> BASIC = register("basic", BasicAmmoType::new);

    @SuppressWarnings("SameParameterValue")
    private static <T extends AmmoType> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return AMMO_TYPES.register(name, supplier);
    }
}
