package com.qtech.bubbles.common.entity;

import java.util.HashMap;

public class ModifierType {
    static final HashMap<String, ModifierType> types = new HashMap<>();

    private final String name;

    public ModifierType(String name) {
        if (types.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate modifier detected!");
        }

        ModifierType.types.put(name, this);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
