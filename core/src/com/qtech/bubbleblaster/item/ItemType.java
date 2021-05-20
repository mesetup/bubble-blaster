package com.qtech.bubbleblaster.item;

import com.qtech.bubbleblaster.common.RegistryEntry;

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
