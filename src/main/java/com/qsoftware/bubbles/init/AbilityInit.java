package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.QInternalAddon;
import com.qsoftware.bubbles.ability.TeleportAbility;
import com.qsoftware.bubbles.common.ability.AbilityType;
import com.qsoftware.bubbles.registry.DeferredRegister;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused", "SameParameterValue"})
public class AbilityInit {
    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.ABILITIES);
    public static final RegistryObject<AbilityType<TeleportAbility>> TELEPORT_ABILITY = register("teleport", () -> new AbilityType<>(TeleportAbility::new));

    private static <T extends AbilityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return null;
    }

}
