package com.qtech.bubbles.common.interfaces;

import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

public interface StateHolder {
    @NotNull BsonDocument getState();

    void setState(BsonDocument nbt);
}
