package com.qtech.bubbles.entity;

import com.qtech.bubbles.common.AttributeMap;
import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.effect.StatusEffectInstance;
import com.qtech.bubbles.common.gametype.AbstractGameType;
import com.qtech.bubbles.entity.attribute.Attribute;
import com.qtech.bubbles.entity.damage.DamageSource;
import com.qtech.bubbles.entity.modifier.Modifier;
import com.qtech.bubbles.entity.player.PlayerEntity;
import com.qtech.bubbles.entity.types.EntityType;
import com.qtech.bubbles.event.CollisionEvent;
import com.qtech.bubbles.event._common.SubscribeEvent;
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
public abstract class DamageableEntity extends Entity {
    protected float damageValue;
    protected double speed;
    protected double baseSpeed;
    private final List<Modifier> modifiers = new ArrayList<>();

    // Constructor.
    public DamageableEntity(EntityType<?> type, AbstractGameType gameType) {
        super(type, gameType);
    }

    // Properties
    public float getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(float damageValue) {
        this.damageValue = MathHelper.clamp(damageValue, 0f, attributes.getBase(Attribute.MAX_DAMAGE));
    }

    public float getMaxDamageValue() {
        return attributes.getBase(Attribute.MAX_DAMAGE);
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
     * @deprecated Use {@link #damage(double, DamageSource)} instead.
     */
    @Deprecated
    public void damage(double value) {
        if (attributes.getBase(Attribute.DEFENSE) == 0f) {
            this.destroy();
        }

        this.damageValue -= value / attributes.getBase(Attribute.DEFENSE);
        this.checkDamage();
    }

    /**
     * <h1>Attack!!!</h1>
     *
     * @param value  the attack value.
     * @param source the damage source.
     */
    @SuppressWarnings("unused")
    public void damage(double value, DamageSource source) {
        if (attributes.getBase(Attribute.DEFENSE) == 0f) {
            this.destroy();
        }

        this.damageValue -= value / attributes.getBase(Attribute.DEFENSE);
        this.checkDamage();
    }

    public void destroy() {
        this.damageValue = 0;
        checkDamage();
    }

    public void restoreDamage(float value) {
        this.damageValue += value;
        this.damageValue = MathHelper.clamp(damageValue, 0f, attributes.getBase(Attribute.MAX_DAMAGE));
    }

    protected void checkDamage() {
        if (this.damageValue <= 0) {
            this.delete();
        }
    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument state = super.getState();

        BsonArray bases = this.bases.write(new BsonArray());
        state.put("Bases", bases);

        BsonArray attributes = new BsonArray();
        attributes = this.attributes.write(attributes);
        state.put("Attributes", attributes);

        BsonArray effects = new BsonArray();
        for (StatusEffectInstance statusEffectInstance : activeEffects) {
            BsonDocument effectDocument = statusEffectInstance.write(new BsonDocument());
            effects.add(effectDocument);
        }
        state.put("Effects", effects);

        BsonDocument position = new BsonDocument();
        position.put("x", new BsonDouble(getX()));
        position.put("y", new BsonDouble(getY()));
        state.put("position", position);

        state.put("id", new BsonInt64(entityId));
        state.put("name", new BsonString(this.type.getRegistryName().toString()));

        state.put("damageValue", new BsonDouble(damageValue));

        return state;
    }

    @Override
    public void setState(BsonDocument state) {
        BsonArray attributes = state.getArray("Attributes");
        this.attributes = new AttributeMap().read(attributes);

        BsonArray bases = state.getArray("Bases");
        this.bases = new AttributeMap().read(bases);
//        this.baseSpeed = bases.getDouble("Speed").getValue();

        BsonDocument position = state.getDocument("position");
        this.x = (float) position.getDouble("x").getValue();
        this.y = (float) position.getDouble("y").getValue();

        // TODO: Update to locate entity class, from ResourceLocation using EntityType.
        ResourceEntry entityTypeKey = ResourceEntry.fromString(state.getString("Name").getValue());
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
