package com.ultreon.bubbles.ability.triggers.types;

import com.ultreon.hydro.registry.ObjectInit;
import com.ultreon.hydro.common.RegistryEntry;

import java.util.Objects;

public class AbilityKeyTriggerType extends RegistryEntry implements ObjectInit<AbilityKeyTriggerType> {
    public static final AbilityKeyTriggerType HOLD = new AbilityKeyTriggerType();

    private final long hash;

    public AbilityKeyTriggerType() {
        this.hash = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbilityKeyTriggerType that = (AbilityKeyTriggerType) o;
        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hash);
    }
}
