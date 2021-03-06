package com.ultreon.bubbles.common;

import com.ultreon.bubbles.common.holders.IArrayDataHolder;
import com.ultreon.bubbles.entity.attribute.Attribute;
import com.ultreon.commons.function.primitive.BiFloat2FloatFunction;
import com.ultreon.commons.function.primitive.Float2FloatFunction;
import org.bson.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@SuppressWarnings("DuplicatedCode")
public class AttributeMap implements IArrayDataHolder<AttributeMap> {
    private final HashMap<Attribute, Float> map = new HashMap<>();

    public AttributeMap() {

    }

    public void setBase(Attribute attribute, float value) {
        this.map.put(attribute, value);
    }

    public float getBase(Attribute attribute) {
        @Nullable Float value = this.map.get(attribute);
        if (value == null) {
            throw new NoSuchElementException("Attribute \"" + attribute.getName() + "\" has no set base value.");
        }
        return value;
    }

    public void getModified(Attribute attribute, BiFloat2FloatFunction function, List<AttributeMap> attributeMaps) {
        if (!this.map.containsKey(attribute)) {
            throw new NoSuchElementException("Attribute \"" + attribute.getName() + "\" has no set base value.");
        }

        float f = getBase(attribute);
        for (AttributeMap map : attributeMaps) {
            f *= function.apply(f, map.getBase(attribute));
        }
    }

    public void getModified(Attribute attribute, BiFloat2FloatFunction function, AttributeMap... attributeMaps) {
        if (!this.map.containsKey(attribute)) {
            throw new NoSuchElementException("Attribute \"" + attribute.getName() + "\" has no set base value.");
        }

        float f = getBase(attribute);
        for (AttributeMap map : attributeMaps) {
            f *= function.apply(f, map.getBase(attribute));
        }
    }

    public void getModified(Attribute attribute, List<Float2FloatFunction> functions) {
        if (!this.map.containsKey(attribute)) {
            throw new NoSuchElementException("Attribute \"" + attribute.getName() + "\" has no set base value.");
        }

        float f = getBase(attribute);
        for (Float2FloatFunction function : functions) {
            f *= function.apply(f);
        }
    }

    public void getModified(Attribute attribute, Float2FloatFunction... functions) {
        if (!this.map.containsKey(attribute)) {
            throw new NoSuchElementException("Attribute \"" + attribute.getName() + "\" has no set base value.");
        }

        float f = getBase(attribute);
        for (Float2FloatFunction function : functions) {
            f *= function.apply(f);
        }
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
            if (item instanceof BsonDocument document) {
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
