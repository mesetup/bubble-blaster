package com.qtech.bubbles.item;

import com.qtech.bubbles.registry.RegistryEntry;

public abstract class ItemType extends RegistryEntry implements IItemProvider {
    @Override
    public ItemType getItem() {
        return this;
    }

    @SuppressWarnings("EmptyMethod")
    public void onEntityTick() {

    }

    @SuppressWarnings("EmptyMethod")
    public void tick() {

    }
}
