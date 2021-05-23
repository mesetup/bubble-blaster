package com.qtech.bubbleblaster.common.ability;

import com.qtech.bubbleblaster.common.RegistryEntry;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @param <T>
 */
public class AbilityType<T extends Ability<T>> extends RegistryEntry {
    // Types
//    public static final AbilityType<TeleportAbility> TELEPORT_ABILITY = new AbilityType<>(TeleportAbility.class, "teleport_ability");

    // Fields
    private final Supplier<T> ability;

    public AbilityType(Supplier<T> ability) {
        this.ability = ability;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbilityType<?> that = (AbilityType<?>) o;
        return Objects.equals(getRegistryName(), that.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public T getAbility() {
        return ability.get();
    }
}
