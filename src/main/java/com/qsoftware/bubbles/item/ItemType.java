package com.qsoftware.bubbles.item;

import com.qsoftware.bubbles.common.RegistryEntry;
import com.qsoftware.bubbles.common.entity.Entity;

public abstract class ItemType extends RegistryEntry implements IItemProvider {
    @Override
    public ItemType getItem() {
        return this;
    }

    public void onEntityTick(Entity entity, Item item) {

    }

    public void tick(Item item) {

    }
}
