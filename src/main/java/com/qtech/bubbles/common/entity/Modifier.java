package com.qtech.bubbles.common.entity;

import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;

public class Modifier {
    public static final ModifierType ATTACK = new ModifierType("qbubbles.attack");
    public static final ModifierType DEFENSE = new ModifierType("qbubbles.defense");
    public static final ModifierType SCORE = new ModifierType("qbubbles.score");

    private final ModifierType type;
    private final double value;

    public Modifier(ModifierType type, double value) {
        this.type = type;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public BsonDocument write(BsonDocument document) {
        document.put("name", new BsonString(this.type.getName()));
        document.put("value", new BsonDouble(this.value));

        return null;
    }

    public static void read(BsonDocument document) {
        ModifierType type = ModifierType.types.get(document.getString("name").getValue());
        double value = document.getDouble("value").getValue();
    }
}