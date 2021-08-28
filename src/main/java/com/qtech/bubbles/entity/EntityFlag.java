package com.qtech.bubbles.entity;

public class EntityFlag {
    public static final EntityFlag MOTION_ENABLED = new EntityFlag("qbubbles.motion_enabled");

    private final String name;

    public EntityFlag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
