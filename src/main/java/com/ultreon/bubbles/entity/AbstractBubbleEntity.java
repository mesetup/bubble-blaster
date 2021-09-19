package com.ultreon.bubbles.entity;

import com.ultreon.bubbles.common.gametype.AbstractGameType;
import com.ultreon.bubbles.entity.types.EntityType;
import com.ultreon.bubbles.environment.Environment;
import com.ultreon.bubbles.event.EntityCollisionEvent;
import com.ultreon.hydro.common.ResourceEntry;
import com.ultreon.hydro.event.SubscribeEvent;
import com.ultreon.hydro.registry.Registry;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * <h1>ItemType Entity base class</h1>
 * For entities such as a {@link BubbleEntity bubble}
 *
 * @see Entity
 */
@SuppressWarnings("unused")
public abstract class AbstractBubbleEntity extends DamageableEntity {
    protected double speed;
    protected double baseSpeed;

    // Constructor
    public AbstractBubbleEntity(EntityType<?> type, AbstractGameType gameType) {
        super(type, gameType);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    @Override
    @SubscribeEvent
    public void onCollision(EntityCollisionEvent obj) {

    }

    @Override
    public void onSpawn(Point pos, Environment environment) {
        this.damageValue = getMaxDamageValue();
    }

    public void restoreDamage(double value) {
        if (damageValue + value > getMaxDamageValue()) {
            this.damageValue = getMaxDamageValue();
            return;
        }
        this.damageValue += value;
    }

    @Override
    public @NotNull BsonDocument getState() {
        return super.getState();
    }

    @Override
    public void setState(BsonDocument state) {
        super.setState(state);

        BsonDocument attributes = state.getDocument("attributes");
        this.speed = attributes.getDouble("speed").doubleValue();

        BsonDocument bases = state.getDocument("bases");
        this.baseSpeed = bases.getDouble("speed").doubleValue();

        BsonDocument position = state.getDocument("position");
        this.x = (float) position.getDouble("x").getValue();
        this.y = (float) position.getDouble("y").getValue();

        ResourceEntry entityTypeKey = ResourceEntry.fromString(state.getString("key").getValue());
        this.type = Registry.getRegistry(EntityType.class).get(entityTypeKey);
    }

    public String toSimpleString() {
        return getRegistryName() + "@(" + Math.round(getX()) + "," + Math.round(getY()) + ")";
    }

    public String toAdvancedString() {
        BsonDocument nbt = getState();
        String data = nbt.toString();

        return getRegistryName() + "@" + data;
    }
}
