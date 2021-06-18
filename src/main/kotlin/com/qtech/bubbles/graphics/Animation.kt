package com.qtech.bubbles.graphics

class Animation(
    private val valueStart: Double, private val valueEnd: Double, private val duration: Double
) {
    // Flags
    private var active = false
    private var timeStart = 0.0
    private var timeEnd = 0.0
    fun start() {
        active = true
        timeStart = System.nanoTime().toDouble() / 1000000000.0
        timeEnd = timeStart + duration
    }

    fun animate(): Double {
        if (active) {
            val currentTime = System.nanoTime().toDouble() / 1000000000.0
            var now: Double
            now = if (timeEnd - timeStart != 0.0) {
                ((currentTime - timeEnd) / (timeEnd - timeStart) + 1) / 2
            } else {
                1.0
            }
            if (now >= 1) now = 1.0 else if (now <= 0) now = 0.0
            return now * (valueEnd - valueStart) + valueStart
        }
        return 0.0
    }

    val isEnded: Boolean
        get() {
            val currentTime = System.nanoTime().toDouble() / 1000000000.0
            val now = ((currentTime - timeEnd) / (timeEnd - timeStart) + 1) / 2
            return now >= 1
        }
}