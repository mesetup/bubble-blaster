package com.qtech.bubbles.entity;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.environment.Environment;
import com.qtech.bubbles.event.CollisionEvent;
import com.qtech.bubbles.event._common.SubscribeEvent;
import com.qtech.bubbles.registry.Registry;
import org.bson.*;
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
    public void onCollision(CollisionEvent obj) {

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
        BsonDocument superState = super.getState();

        superState.put("hardness", new BsonDouble(damageValue));

        BsonArray bases = this.bases.write(new BsonArray());
        superState.put("Bases", bases);

        BsonArray attributes = new BsonArray();
        attributes = this.attributes.write(attributes);
        superState.put("Attributes", attributes);

        BsonArray effects = new BsonArray();
        for (StatusEffectInstance statusEffectInstance : activeEffects) {
            BsonDocument effectDocument = statusEffectInstance.write(new BsonDocument());
            effects.add(effectDocument);
        }
        superState.put("Effects", effects);

        BsonDocument position = new BsonDocument();
        position.put("x", new BsonDouble(getX()));
        position.put("y", new BsonDouble(getY()));
        superState.put("Position", position);

        superState.put("id", new BsonInt64(entityId));
        superState.put("entityType", new BsonString(this.type.getRegistryName().toString()));

        return superState;
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
