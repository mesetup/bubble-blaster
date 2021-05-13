package com.qsoftware.bubbles.common.entity.spawn;

import com.qsoftware.bubbles.entity.types.EntityType;
import com.qsoftware.bubbles.registry.Registry;

import java.util.Collection;

public class Spawner {
    public Spawner() {
        Collection<EntityType> entityTypes = Registry.getRegistry(EntityType.class).values();

        for (EntityType entityType : entityTypes) {
            entityType.getRegistryName();
        }
    }
}
