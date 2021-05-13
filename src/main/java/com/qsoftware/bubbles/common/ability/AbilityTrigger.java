package com.qsoftware.bubbles.common.ability;

import com.qsoftware.bubbles.common.entity.Entity;

public abstract class AbilityTrigger {
    private final AbilityTriggerType type;
    private final Entity entity;

    protected AbilityTrigger(AbilityTriggerType type, Entity entity) {
        this.type = type;
        this.entity = entity;
    }

    public AbilityTriggerType getType() {
        return type;
    }

    public Entity getEntity() {
        return entity;
    }
}
