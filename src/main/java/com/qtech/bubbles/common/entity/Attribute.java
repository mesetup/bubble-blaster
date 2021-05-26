package com.qtech.bubbles.common.entity;

import java.util.HashMap;

public class Attribute {
    private static final HashMap<String, Attribute> attributeMap = new HashMap<>();

    public static final Attribute SCORE_MULTIPLIER = new Attribute("bubbleblaster.score_multiplier");
    public static final Attribute MAX_DAMAGE = new Attribute("bubbleblaster.max_health");
    public static final Attribute DEFENSE = new Attribute("bubbleblaster.defense");
    public static final Attribute ATTACK = new Attribute("bubbleblaster.attack");
    public static final Attribute SPEED = new Attribute("bubbleblaster.speed");
    public static final Attribute LUCK = new Attribute("bubbleblaster.luck");

    private final String name;

    public Attribute(String name) {
        if (Attribute.attributeMap.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate attribute detected!");
        }

        Attribute.attributeMap.put(name, this);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Attribute fromName(String name) {
        return Attribute.attributeMap.get(name);
    }
}
