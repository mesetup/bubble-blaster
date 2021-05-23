package com.qtech.bubbleblaster.ability.triggers.types;

import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.init.ObjectInit;

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
