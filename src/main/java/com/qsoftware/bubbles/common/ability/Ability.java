package com.qsoftware.bubbles.common.ability;

import com.qsoftware.bubbles.ability.triggers.AbilityKeyTrigger;
import com.qsoftware.bubbles.ability.triggers.types.AbilityKeyTriggerType;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.interfaces.StateHolder;
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

    public abstract int getTriggerKey();

    /**
     * Method for the trigger type of the ability.
     *
     * @return the trigger type.
     */
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

    public void onEntityTick(Entity entity) {
        if (isRegenerateable()) {
            this.value += getRegenerationSpeed();
        }
    }

    @Override
    public void setState(BsonDocument nbt) throws IllegalAccessException {
        this.cooldown = nbt.getInt32("Cooldown").getValue();
        this.value = nbt.getInt32("Value").getValue();
    }

    public abstract void trigger(AbilityTrigger trigger);

    public abstract void triggerEntity(Entity entity);

    public abstract boolean isTriggerable(@SuppressWarnings("unused") Entity entity);

    public abstract boolean isRegenerateable();

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
