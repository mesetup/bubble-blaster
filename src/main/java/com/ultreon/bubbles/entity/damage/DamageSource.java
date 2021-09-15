package com.ultreon.bubbles.entity.damage;

import com.ultreon.bubbles.entity.Entity;

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
