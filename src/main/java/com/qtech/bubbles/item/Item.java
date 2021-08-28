package com.qtech.bubbles.item;

import com.qtech.bubbles.common.ResourceEntry;
import com.qtech.bubbles.common.TagHolder;
import com.qtech.bubbles.common.interfaces.StateHolder;
import com.qtech.bubbles.entity.Entity;
import com.qtech.bubbles.registry.Registers;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.jetbrains.annotations.NotNull;

public final class Item implements IItemProvider, StateHolder, TagHolder {
    private ItemType type;
    private BsonDocument tag;

    @Override
    public ItemType getItem() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public @NotNull BsonDocument getState() {
        BsonDocument document = new BsonDocument();
        document.put("Type", new BsonString(type.getRegistryName().toString()));
        document.put("Tag", tag);

        return document;
    }

    @Override
    public void setState(BsonDocument nbt) {
        this.type = Registers.ITEMS.get(ResourceEntry.fromString(nbt.getString("Type").getValue()));
        this.tag = nbt.getDocument("Tag");
    }

    @Override
    public BsonDocument getTag() {
        return tag;
    }

    public void onEntityTick(Entity entity) {
        this.type.onEntityTick();
    }

    public void tick() {
        this.type.tick();
    }
}
