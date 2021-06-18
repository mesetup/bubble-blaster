package com.qtech.bubbles.common.logging

class GameLogRecord(val message: String, val logger: GameLogger, val level: GameLogLevel) {
    val nanoTime: Long = System.nanoTime()
    val milliTime: Long = System.currentTimeMillis()
    val loggerName: String
        get() = logger.name
}