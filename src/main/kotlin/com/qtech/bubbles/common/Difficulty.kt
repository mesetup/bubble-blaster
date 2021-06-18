package com.qtech.bubbles.common

enum class Difficulty(val defaultLocal: Float) {
    BABY(0.0625f),  // Too easy
    EASY(0.5f),
    NORMAL(1.0f),
    HARD(2.0f),
    EXPERT(4.0f),
    APOCALYPSE(8.0f),
    IMPOSSIBLE(256.0f);

}