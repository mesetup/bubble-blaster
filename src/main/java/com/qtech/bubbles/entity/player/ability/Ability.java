package com.qtech.bubbles.entity.player.ability;

import com.qtech.bubbles.ability.triggers.AbilityKeyTrigger;
import com.qtech.bubbles.ability.triggers.types.AbilityKeyTriggerType;
import com.qtech.bubbles.common.interfaces.StateHolder;
import com.qtech.bubbles.entity.Entity;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.jetbrains.annotations.NotNull;

public abstract class Ability<T extends Ability<T>> implements StateHolder {
    private final AbilityType<T> type;
    private int cooldown;
    private int value;

    public Ability(AbilityType<T> type) {
        this.type = type;
    }

    @SuppressWarnings("SameReturnValue")
    public abstract int getTriggerKey();

    /**
     * Method for the trigger type of the ability.
     *
     * @return the trigger type.
     */
    @SuppressWarnings("SameReturnValue")
    public abstract AbilityTriggerType getTriggerType();

    /**
     * Method for have a key trigger for the Ability.
     *
     * @return the key trigger type. Null for no key trigger.
     */
    public AbilityKeyTriggerType getKeyTriggerType() {
        return null;
    }

    /**
     * Method for key trigger event.
     *
     * @param trigger the key trigger.
     */
    public void onKeyTrigger(AbilityKeyTrigger trigger) {

    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument document = new BsonDocument();
        document.put("Cooldown", new BsonInt32(this.cooldown));
        document.put("Value", new BsonInt32(this.value));

        return document;
    }

    public void onEntityTick() {
        if (isRegenerateable()) {
            this.value += getRegenerationSpeed();
        }
    }

    @Override
    public void setState(BsonDocument nbt) {
        this.cooldown = nbt.getInt32("Cooldown").getValue();
        this.value = nbt.getInt32("Value").getValue();
    }

    public abstract void trigger(AbilityTrigger trigger);

    @SuppressWarnings("EmptyMethod")
    public abstract void triggerEntity();

    public abstract boolean isTriggerable(@SuppressWarnings("unused") Entity entity);

    @SuppressWarnings("SameReturnValue")
    public abstract boolean isRegenerateable();

    @SuppressWarnings("SameReturnValue")
    public int getRegenerationSpeed() {
        return 1;
    }

    public AbilityType<T> getType() {
        return type;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getValue() {
        return value;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void subtractValue(int amount) {
        this.value -= amount;
    }

    public void addValue(int amount) {
        this.value -= amount;
    }
}
