package com.qsoftware.bubbles.entity.types;

import com.qsoftware.bubbles.common.RegistryEntry;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.gametype.AbstractGameType;
import com.qsoftware.bubbles.common.scene.Scene;
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

    public T create(Scene scene, AbstractGameType gameType) {
        return entityFactory.create(scene, gameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    public T create(Scene scene, AbstractGameType gameType, BsonDocument document) {
        T t = entityFactory.create(scene, gameType);
        t.setState(document);
        return t;
    }
}
