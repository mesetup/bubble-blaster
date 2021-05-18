package com.qtech.bubbles.common.entity;

import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.BubbleEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.environment.Environment;
import com.qtech.bubbles.event.CollisionEvent;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.registry.Registry;
import com.qtech.bubbles.util.helpers.MathHelper;
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
public abstract class AbstractBubbleEntity extends Entity {
    protected double hardness;
    protected double maxHardness;
    protected double speed;
    protected double baseSpeed;

    // Constructor
    public AbstractBubbleEntity(EntityType<?> type, AbstractGameType gameType) {
        super(type, gameType);
    }

    // Properties.
    public double getHardness() {
        return hardness;
    }

    public void setHardness(double hardness) {
        this.hardness = MathHelper.clamp(hardness, 0, maxHardness);
    }

    public double getMaxHardness() {
        return maxHardness;
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
        this.hardness = maxHardness;
    }

    @Deprecated
    public void damage(double value) {
        if (attributes.get(Attribute.DEFENSE) == 0f) {
            this.instantDestroy();
        }

        this.hardness -= value / attributes.get(Attribute.DEFENSE);
        checkHardness();
    }


    public void damage(double value, DamageSource source) {
        if (attributes.get(Attribute.DEFENSE) == 0f) {
            this.instantDestroy();
        }

        if (source.getEntity().isCollidingWith(this) && isCollidingWith(source.getEntity())) {
            this.hardness -= value / attributes.get(Attribute.DEFENSE);
            checkHardness();
        }
    }

    public void instantDestroy() {
        this.hardness = 0;
        checkHardness();
    }

    public void repair(double value) {
        if (hardness + value > maxHardness) {
            this.hardness = maxHardness;
            return;
        }
        this.hardness += value;
    }

    protected void checkHardness() {
        if (this.hardness <= 0) {
            this.delete();
        }
    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument document = super.getState();

        document.put("hardness", new BsonDouble(hardness));

        BsonArray bases = this.bases.write(new BsonArray());
        document.put("Bases", bases);

        BsonArray attributes = new BsonArray();
        attributes = this.attributes.write(attributes);
        document.put("Attributes", attributes);

        BsonArray effects = new BsonArray();
        for (EffectInstance effectInstance : activeEffects) {
            BsonDocument effectDocument = effectInstance.write(new BsonDocument());
            effects.add(effectDocument);
        }
        document.put("Effects", effects);

        BsonDocument position = new BsonDocument();
        position.put("x", new BsonDouble(getX()));
        position.put("y", new BsonDouble(getY()));
        document.put("Position", position);

        document.put("id", new BsonInt64(entityId));
        document.put("entityType", new BsonString(this.type.getRegistryName().toString()));

        return document;
    }

    @Override
    public void setState(BsonDocument document) {
        BsonDocument attributes = document.getDocument("attributes");
        this.speed = attributes.getDouble("speed").doubleValue();
        this.hardness = attributes.getDouble("hardness").doubleValue();

        BsonDocument bases = document.getDocument("bases");
        this.baseSpeed = bases.getDouble("speed").doubleValue();
        this.maxHardness = bases.getDouble("hardness").doubleValue();

        BsonDocument position = document.getDocument("position");
        this.x = (float) position.getDouble("x").getValue();
        this.y = (float) position.getDouble("y").getValue();

        ResourceLocation entityTypeKey = ResourceLocation.fromString(document.getString("key").getValue());
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
