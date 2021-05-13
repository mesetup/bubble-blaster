package com.qsoftware.bubbles.common.logging.old;

import java.util.logging.Level;

public class GameLoggingLevels extends Level {
    public static final Level DEBUG = (Level) new GameLoggingLevels("DEBUG", 1);

    protected GameLoggingLevels(String name, int value) {
        super(name, value);
    }

    protected GameLoggingLevels(String name, int value, String resourceBundleName) {
        super(name, value, resourceBundleName);
    }
}
