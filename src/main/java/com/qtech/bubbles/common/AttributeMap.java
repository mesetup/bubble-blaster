package com.qtech.bubbles.common;

import com.qtech.bubbles.common.entity.Attribute;
import com.qtech.bubbles.common.holders.IArrayDataHolder;
import org.bson.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AttributeMap implements IArrayDataHolder<AttributeMap> {
    private final HashMap<Attribute, Float> map = new HashMap<>();

    public AttributeMap() {

    }

    public void set(Attribute attribute, float value) {
        this.map.put(attribute, value);
    }

    public float get(Attribute attribute) {
        @Nullable Float value = this.map.get(attribute);
        if (value == null) {
            throw new NullPointerException("Attribute \"" + attribute.getName() + "\" is not defined!");
        }
        return value;
    }

    public BsonArray write(BsonArray array) {
        for (Map.Entry<Attribute, Float> entry : this.map.entrySet()) {
            BsonDocument document = new BsonDocument();
            document.put("name", new BsonString(entry.getKey().getName()));
            document.put("value", new BsonDouble(entry.getValue()));
            array.add(document);
        }
        return array;
    }

    public AttributeMap read(BsonArray array) {
        for (BsonValue item : array.getValues()) {
            if (item instanceof BsonDocument) {
                BsonDocument document = (BsonDocument) item;
                Attribute key = Attribute.fromName(document.getString("name").getValue());
                float value = (float) document.getDouble("value").getValue();

                this.map.put(key, value);
            }
        }
        return this;
    }

    public void setAll(AttributeMap defaultAttributes) {
        this.map.putAll(defaultAttributes.map);
    }
}
