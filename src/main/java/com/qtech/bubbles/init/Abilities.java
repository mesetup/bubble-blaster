package com.qtech.bubbles.init;

import com.qtech.bubbles.InternalAddon;
import com.qtech.bubbles.ability.TeleportAbility;
import com.qtech.bubbles.entity.player.ability.AbilityType;
import com.qtech.bubbles.registry.DeferredRegister;
import com.qtech.bubbles.registry.Registers;
import com.qtech.bubbles.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused", "SameParameterValue"})
public class Abilities {
    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(InternalAddon.ADDON_ID, Registers.ABILITIES);
    public static final RegistryObject<AbilityType<TeleportAbility>> TELEPORT_ABILITY = register("teleport", () -> new AbilityType<>(TeleportAbility::new));

    private static <T extends AbilityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ABILITY_TYPES.register(name, supplier);
    }

}
