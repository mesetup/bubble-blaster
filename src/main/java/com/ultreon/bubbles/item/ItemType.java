package com.ultreon.bubbles.item;

import com.ultreon.hydro.common.RegistryEntry;

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
