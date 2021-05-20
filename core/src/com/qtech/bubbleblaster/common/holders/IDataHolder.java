package com.qtech.bubbleblaster.common.holders;

import org.bson.BsonDocument;

public interface IDataHolder {
    BsonDocument write(BsonDocument document);

    void read(BsonDocument document);
}