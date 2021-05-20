package com.qtech.bubbleblaster.entity.types;

import com.qtech.bubbleblaster.common.RegistryEntry;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
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
}