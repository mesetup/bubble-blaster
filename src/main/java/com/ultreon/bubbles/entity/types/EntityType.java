package com.ultreon.bubbles.entity.types;

import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.hydro.common.RegistryEntry;
import org.bson.BsonDocument;

import java.util.Objects;

public class EntityType<T extends Entity> extends RegistryEntry {
    private final EntityFactory<T> entityFactory;

    public EntityType(EntityFactory<T> entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityType<?> that = (EntityType<?>) o;
        return Objects.equals(getRegistryName(), that.getRegistryName());
    }

    public T create(AbstractGameType gameType) {
        return entityFactory.create(gameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public T create(AbstractGameType gameType, BsonDocument document) {
        T t = entityFactory.create(gameType);
        t.setState(document);
        return t;
    }

    @Override
    public String toString() {
        return "EntityType[" + getRegistryName().toString() + "]";
    }
}
