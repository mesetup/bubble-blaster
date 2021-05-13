package com.qsoftware.bubbles.common.entity;

public final class DamageSource {
    private final DamageSourceType type;
    private final Entity entity;

    public DamageSource(Entity entity, DamageSourceType type) {
        this.entity = entity;
        this.type = type;
    }

    public Entity getEntity() {
        return entity;
    }

    public DamageSourceType getType() {
        return type;
    }
}
