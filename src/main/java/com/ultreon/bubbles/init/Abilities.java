package com.ultreon.bubbles.init;

import com.ultreon.bubbles.InternalAddon;
import com.ultreon.bubbles.ability.TeleportAbility;
import com.ultreon.bubbles.entity.player.ability.AbilityType;
import com.ultreon.hydro.registry.DeferredRegister;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.hydro.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused", "SameParameterValue"})
public class Abilities {
    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(InternalAddon.ADDON_ID, Registers.ABILITIES);
    public static final RegistryObject<AbilityType<TeleportAbility>> TELEPORT_ABILITY = register("teleport", () -> new AbilityType<>(TeleportAbility::new));

    private static <T extends AbilityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ABILITY_TYPES.register(name, supplier);
    }

}
