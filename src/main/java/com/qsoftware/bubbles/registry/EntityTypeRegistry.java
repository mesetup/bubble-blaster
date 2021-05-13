package com.qsoftware.bubbles.registry;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.entity.types.EntityType;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Entity Registry.
 *
 * @see AbstractRegistry
 * @see EntityType
 * @see ResourceLocation
 * @deprecated use {@link Registry <EntityType>} instead.
 */
@Deprecated
public class EntityTypeRegistry extends AbstractRegistry<ResourceLocation, EntityType> {
    protected static EntityTypeRegistry INSTANCE = new EntityTypeRegistry();

    public static EntityTypeRegistry instance() {
        return INSTANCE;
    }

    public EntityTypeRegistry() {
        super();
    }

    @Override
    public EntityType get(ResourceLocation obj) {
        return this.registry.get(obj);
    }

    public EntityType get(String key) {
        return this.registry.get(ResourceLocation.fromString(key));
    }

    public void register(ResourceLocation key, EntityType val) {
        this.registry.put(key, val);
    }

    public void register(ResourceLocation key, Entity val) {
        this.registry.put(key, val.getType());
    }

    public void register(ResourceLocation key, Supplier<? extends Entity> val) {
        this.registry.put(key, val.get().getType());
    }

    @Override
    public Collection<EntityType> values() {
        return registry.values();
    }

    @Override
    public Set<ResourceLocation> keys() {
        return registry.keySet();
    }

    @Override
    public Set<Map.Entry<ResourceLocation, EntityType>> entries() throws IllegalAccessException {
        return null;
    }
}
