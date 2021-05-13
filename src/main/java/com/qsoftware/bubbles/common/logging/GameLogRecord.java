package com.qsoftware.bubbles.common.logging;

import org.jetbrains.annotations.NotNull;

public final class GameLogRecord {
    private final long nanoTime;
    private final long milliTime;
    private final String message;
    private final GameLogger logger;
    private final GameLogLevel level;

    public GameLogRecord(String message, GameLogger logger, @NotNull GameLogLevel level) {
        this.logger = logger;
        this.nanoTime = System.nanoTime();
        this.milliTime = System.currentTimeMillis();
        this.message = message;
        this.level = level;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    public long getMilliTime() {
        return milliTime;
    }

    public String getMessage() {
        return message;
    }

    public GameLogger getLogger() {
        return logger;
    }

    public String getLoggerName() {
        return getLogger().getName();
    }

    public GameLogLevel getLevel() {
        return level;
    }
}
