package com.qtech.bubbles.common;

public enum Difficulty {
    BABY(0.0625f),  // Too easy
    EASY(0.5f),
    NORMAL(1.0f),
    HARD(2.0f),
    EXPERT(4.0f),
    APOCALYPSE(8.0f),
    IMPOSSIBLE(256.0f);

    private final float defaultLocal;

    Difficulty(float defaultLocal) {
        this.defaultLocal = defaultLocal;
    }

    public float getDefaultLocal() {
        return defaultLocal;
    }
}
