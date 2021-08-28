package com.qtech.bubbles.common;

/**
 * Difficulty enum, used as api for difficulty information.
 *
 * @author Quinten
 * @since 1.0.0
 */
public enum Difficulty {
    BABY(0.0625f),  // Too easy
    EASY(0.5f),
    NORMAL(1.0f),
    HARD(2.0f),
    EXPERT(4.0f),
    APOCALYPSE(8.0f),
    IMPOSSIBLE(256.0f);

    private final float plainModifier;

    Difficulty(float modifier) {
        this.plainModifier = modifier;
    }

    public float getPlainModifier() {
        return plainModifier;
    }
}
