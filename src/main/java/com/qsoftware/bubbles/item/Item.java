package com.qsoftware.bubbles.item;

import com.qsoftware.bubbles.common.ResourceLocation;
import com.qsoftware.bubbles.common.TagHolder;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.interfaces.StateHolder;
import com.qsoftware.bubbles.registry.Registers;
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
    public void setState(BsonDocument nbt) throws IllegalAccessException {
        this.type = Registers.ITEMS.get(ResourceLocation.fromString(nbt.getString("Type").getValue()));
        this.tag = nbt.getDocument("Tag");
    }

    @Override
    public BsonDocument getTag() {
        return tag;
    }

    public void onEntityTick(Entity entity) {
        this.type.onEntityTick(entity, this);
    }

    public void tick() {
        this.type.tick(this);
    }
}
