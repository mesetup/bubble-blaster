package com.ultreon.bubbles.entity.player.ability;

import com.ultreon.hydro.common.RegistryEntry;
import com.ultreon.hydro.registry.ObjectInit;

import java.util.Objects;

public class AbilityTriggerType extends RegistryEntry implements ObjectInit<AbilityTriggerType> {
    public static final AbilityTriggerType KEY_TRIGGER = new AbilityTriggerType();
    private final long hash;

    public AbilityTriggerType() {
        this.hash = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbilityTriggerType that = (AbilityTriggerType) o;
        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hash);
    }
}
