package com.qtech.bubbleblaster.common.holders;

import org.bson.BsonArray;

public interface IArrayDataHolder<T> {
    BsonArray write(BsonArray array);

    T read(BsonArray array);
}
