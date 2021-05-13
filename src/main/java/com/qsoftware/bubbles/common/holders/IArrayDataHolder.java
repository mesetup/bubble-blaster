package com.qsoftware.bubbles.common.holders;

import org.bson.BsonArray;

public interface IArrayDataHolder<T> {
    BsonArray write(BsonArray array);

    T read(BsonArray array);
}
