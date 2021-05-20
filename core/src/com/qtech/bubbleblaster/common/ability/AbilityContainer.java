package com.qtech.bubbleblaster.common.ability;

import com.qtech.bubbleblaster.common.entity.Entity;

import java.util.HashMap;

public class AbilityContainer {
    private final HashMap<AbilityType<? extends Ability<?>>, Ability<?>> abilities = new HashMap<>();
    private final Entity entity;
    private Ability<?> currentAbility;

    public AbilityContainer(Entity entity) {
        this.entity = entity;
    }

    public void add(Ability<? extends Ability<?>> ability) {
        this.abilities.put(ability.getType(), ability);
    }

    public void remove(AbilityType<? extends Ability<?>> abilityType) {
        this.abilities.remove(abilityType);
    }

    public void setCurrent(AbilityType<? extends Ability<?>> abilityType) {
        this.currentAbility = abilities.get(abilityType);
    }

    public Ability<?> getCurrent() {
        return currentAbility;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void onEntityTick() {
        if (this.currentAbility != null) {
            this.currentAbility.onEntityTick();
        }
    }

    public Ability<?> get(AbilityType<? extends Ability<?>> type) {
        return this.abilities.get(type);
    }
}
