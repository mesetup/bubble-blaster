package com.qtech.bubbleblaster.data;

import com.qtech.bubbleblaster.common.entity.Entity;
import org.bson.BsonDocument;
import org.bson.BsonDouble;


public class DataManager {
    public BsonDocument storeEntity(Entity entity) {
        BsonDocument nbt = new BsonDocument();
        nbt.put("position", storePosition(entity.getPos()));
        nbt.put("data", entity.getState());
        return nbt;
    }

    private BsonDocument storePosition(Point2D pos) {
        BsonDocument nbt = new BsonDocument();
        nbt.put("x", new BsonDouble(pos.getX()));
        nbt.put("y", new BsonDouble(pos.getY()));
        return nbt;
    }
}
