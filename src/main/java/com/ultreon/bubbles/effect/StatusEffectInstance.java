package com.ultreon.bubbles.effect;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.common.TagHolder;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.registry.Registers;
import com.ultreon.bubbles.util.BsonUtils;
import com.ultreon.bubbles.util.helpers.MathHelper;
import com.ultreon.commons.annotation.FieldsAreNonnullByDefault;
import com.ultreon.commons.annotation.MethodsReturnNonnullByDefault;
import com.ultreon.commons.utilities.python.builtins.ValueError;
import com.ultreon.hydro.common.ResourceEntry;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StatusEffectInstance implements TagHolder {
    private final StatusEffect type;
    private int strength;
    private long endTime;

    private boolean active;
    private BsonDocument tag = new BsonDocument();
    private long baseDuration;

    /**
     * @throws ClassCastException when effect couldn't be recognized.
     */
    private StatusEffectInstance(BsonDocument document) {
        this.tag = BsonUtils.getTagDocument(document);
        this.type = Registers.EFFECTS.get(ResourceEntry.fromString(document.getString("Name").getValue()));
        this.setRemainingTime(document.getInt64("Duration").getValue());
        this.baseDuration = document.getInt64("BaseDuration").getValue();
    }

    public StatusEffectInstance(StatusEffect type, long duration, int strength) throws ValueError {
        if (strength < 1) {
            throw new ValueError("Cannot create effect instance with strength < 1");
        }

        this.type = type;
        this.strength = strength;
        this.setRemainingTime(duration);
    }

    public boolean allowMerge() {
        return true;
    }

    public final void start(Entity entity) {
        onStart(entity);
        BubbleBlaster.getEventBus().subscribe(this);

        active = true;
    }

    public final void stop(Entity entity) {
        onStop(entity);
        try {
            BubbleBlaster.getEventBus().unsubscribe(this);
        } catch (IllegalArgumentException ignored) {

        }

        active = false;
    }

    public final StatusEffect getType() {
        return type;
    }

    public void tick(Entity entity) {
        if (this.getRemainingTime() <= 0d) {
            this.active = false;
            this.stop(entity);
        } else {
            this.type.tick(entity, this);
        }
    }

    public void onStart(Entity entity) {
        this.type.onStart(this, entity);
    }

    public void onStop(Entity entity) {
        this.type.onStop(entity);
    }

    @SuppressWarnings("EmptyMethod")
    protected void updateStrength(int old, int _new) {

    }

    public void addStrength() {
        int old = getStrength();
        byte output = (byte) (this.strength + 1);
        this.strength = MathHelper.clamp(output, 1, 255);
        updateStrength(old, getStrength());
    }

    public void addStrength(byte amount) {
        int old = getStrength();
        byte output = (byte) (this.strength + amount);
        this.strength = MathHelper.clamp(output, 1, 255);
        updateStrength(old, getStrength());
    }

    public void removeStrength() {
        int old = getStrength();
        byte output = (byte) (this.strength - 1);
        this.strength = MathHelper.clamp(output, 1, 255);
        updateStrength(old, getStrength());
    }

    public void removeStrength(byte amount) {
        int old = getStrength();
        byte output = (byte) (this.strength - amount);
        this.strength = MathHelper.clamp(output, 1, 255);
        updateStrength(old, getStrength());
    }

    public final int getStrength() {
        return this.strength;
    }

    public void setStrength(byte strength) throws ValueError {
        int old = getStrength();
        if (strength < 1) {
            throw new ValueError("Tried to set strength less than 1.");
        }

        this.strength = strength;
        updateStrength(old, getStrength());
    }

    public final long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getRemainingTime() {
        return (this.endTime - System.currentTimeMillis()) / 1000;
    }

    public void setRemainingTime(long time) {
        this.endTime = System.currentTimeMillis() + time * 1000;
    }

    public void addTime(long time) {
        this.setRemainingTime(this.getRemainingTime() + time);
    }

    public void removeTime(long time) {
        this.setRemainingTime(this.getRemainingTime() - time);
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        StatusEffectInstance statusEffectInstance = (StatusEffectInstance) o;
        return Objects.equals(this.getType(), statusEffectInstance.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }

    public BsonDocument write(BsonDocument document) {
        document.put("BaseDuration", new BsonInt64(getBaseDuration()));
        document.put("Duration", new BsonInt64(getRemainingTime()));
        document.put("Strength", new BsonInt32(getStrength()));

        return document;
    }

    @Override
    public BsonDocument getTag() {
        return this.tag;
    }

    public long getStartTime() {
        return this.getEndTime() - this.baseDuration;
    }

    public long getBaseDuration() {
        return this.baseDuration;
    }
}
