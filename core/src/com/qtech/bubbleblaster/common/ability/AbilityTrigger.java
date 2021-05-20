package com.qtech.bubbleblaster.common.ability;

import com.qtech.bubbleblaster.common.entity.Entity;

public abstract class AbilityTrigger {
    private final AbilityTriggerType type;
    private final Entity entity;

    @SuppressWarnings("SameParameterValue")
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
