package com.qtech.bubbles.common.entity;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.ResourceLocation;
import com.qtech.bubbles.common.effect.EffectInstance;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.event.CollisionEvent;
import com.qtech.bubbles.event.SubscribeEvent;
import com.qtech.bubbles.registry.Registry;
import com.qtech.bubbles.util.helpers.MathHelper;
import org.bson.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Living Entity base class.</h1>
 * The class for all living entities, such as {@link PlayerEntity the player}.
 *
 * @see Entity
 */
@SuppressWarnings("unused")
public abstract class LivingEntity extends Entity {
    protected float health;
    protected double speed;
    protected double baseSpeed;
    private final List<Modifier> modifiers = new ArrayList<>();

    // Constructor.
    public LivingEntity(EntityType<?> type, AbstractGameType gameType) {
        super(type, gameType);
    }

    // Properties
    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = MathHelper.clamp(health, 0f, attributes.get(Attribute.MAX_HEALTH));
    }

    public float getMaxHealth() {
        return attributes.get(Attribute.MAX_HEALTH);
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
    public void onCollision(CollisionEvent evtObj) {

    }

    // Attack & heal.

    /**
     * <h1>Attack!!!</h1>
     *
     * @param value The attack value.
     * @deprecated Use {@link #attack(double, DamageSource)} instead.
     */
    @Deprecated
    public void attack(double value) {
        if (attributes.get(Attribute.DEFENSE) == 0f) {
            this.instantDeath();
        }

        this.health -= value / attributes.get(Attribute.DEFENSE);
        this.checkHealth();
    }

    /**
     * <h1>Attack!!!</h1>
     *
     * @param value  the attack value.
     * @param source the damage source.
     */
    @SuppressWarnings("unused")
    public void attack(double value, DamageSource source) {
        if (attributes.get(Attribute.DEFENSE) == 0f) {
            this.instantDeath();
        }

        this.health -= value / attributes.get(Attribute.DEFENSE);
        this.checkHealth();
    }

    public void instantDeath() {
        this.health = 0;
        checkHealth();
    }

    public void heal(float value) {
        this.health += value;
        this.health = MathHelper.clamp(health, 0f, attributes.get(Attribute.MAX_HEALTH));
    }

    protected void checkHealth() {
        if (this.health <= 0) {
            this.delete();
        }
    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument document = super.getState();

        document.put("Health", new BsonDouble(health));

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
        position.put("X", new BsonDouble(getX()));
        position.put("Y", new BsonDouble(getY()));
        document.put("Position", position);

        document.put("ID", new BsonInt64(entityId));
        document.put("Name", new BsonString(this.type.getRegistryName().toString()));

        return document;
    }

    @Override
    public void setState(BsonDocument document) {
        BsonArray attributes = document.getArray("Attributes");
        this.attributes = new AttributeMap().read(attributes);

        BsonArray bases = document.getArray("Bases");
        this.bases = new AttributeMap().read(bases);
//        this.baseSpeed = bases.getDouble("Speed").getValue();

        BsonDocument position = document.getDocument("position");
        this.x = (float) position.getDouble("x").getValue();
        this.y = (float) position.getDouble("y").getValue();

        // TODO: Update to locate entity class, from ResourceLocation using EntityType.
        ResourceLocation entityTypeKey = ResourceLocation.fromString(document.getString("Name").getValue());
//        this.type = EntityTypeRegistry.INSTANCE.get(entityTypeKey);
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
