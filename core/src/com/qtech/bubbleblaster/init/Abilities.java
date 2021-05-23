package com.qtech.bubbleblaster.init;

import com.qtech.bubbleblaster.QInternalAddon;
import com.qtech.bubbleblaster.ability.TeleportAbility;
import com.qtech.bubbleblaster.common.ability.AbilityType;
import com.qtech.bubbleblaster.registry.DeferredRegister;
import com.qtech.bubbleblaster.registry.Registers;
import com.qtech.bubbleblaster.registry.object.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unused", "SameParameterValue"})
public class Abilities {
    public static final DeferredRegister<AbilityType> ABILITY_TYPES = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.ABILITIES);
    public static final RegistryObject<AbilityType<TeleportAbility>> TELEPORT_ABILITY = register("teleport", () -> new AbilityType<>(TeleportAbility::new));

    private static <T extends AbilityType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return ABILITY_TYPES.register(name, supplier);
    }

}
