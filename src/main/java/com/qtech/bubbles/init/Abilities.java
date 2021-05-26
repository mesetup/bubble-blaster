package com.qtech.bubbles.init;

import com.qtech.bubbles.BBInternalAddon;
import com.qtech.bubbles.ability.TeleportAbility;
import com.qtech.bubbles.common.ability.AbilityType;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused", "SameParameterValue"})
public class Abilities {
    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(BBInternalAddon.ADDON_ID, Registers.ABILITIES);
    public static final RegistryObject<AbilityType<TeleportAbility>> TELEPORT_ABILITY = register("teleport", () -> new AbilityType<>(TeleportAbility::new));

    private static <T extends AbilityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ABILITY_TYPES.register(name, supplier);
    }

}
