package com.qsoftware.bubbles.common.effect;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.annotation.FieldsAreNonnullByDefault;
import com.qsoftware.bubbles.annotation.MethodsReturnNonnullByDefault;
import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.TagHolder;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.util.BsonUtils;
import com.qsoftware.bubbles.util.helpers.MathHelper;
import com.qsoftware.utilities.python.builtins.ValueError;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EffectInstance implements TagHolder {
    private final Effect<?> type;
    private int strength;
    private long endTime;

    private boolean active;
    private BsonDocument tag = new BsonDocument();
    private long baseDuration;

    /**
     * @throws ClassCastException when effect couldn't be recognized.
     */
    private EffectInstance(BsonDocument document) {
        this.tag = BsonUtils.getTagDocument(document);
        this.type = Registers.EFFECTS.get(ResourceLocation.fromString(document.getString("Name").getValue()));
        this.setRemainingTime(document.getInt64("Duration").getValue());
        this.baseDuration = document.getInt64("BaseDuration").getValue();
    }

    public EffectInstance(Effect<?> type, long duration, int strength) throws ValueError {
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
        QBubbles.getEventBus().register(this);

        active = true;
    }

    public final void stop(Entity entity) {
        onStop(entity);
        try {
            QBubbles.getEventBus().unregister(this);
        } catch (IllegalArgumentException ignored) {

        }

        active = false;
    }

    public final Effect<?> getType() {
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
        this.type.onStop(this, entity);
    }

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
        EffectInstance effectInstance = (EffectInstance) o;
        return Objects.equals(this.getType(), effectInstance.getType());
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
