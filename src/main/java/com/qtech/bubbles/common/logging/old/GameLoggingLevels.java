package com.qtech.bubbles.common.logging.old;

import java.util.logging.Level;

public class GameLoggingLevels extends Level {
    public static final Level DEBUG = new GameLoggingLevels("DEBUG", 1);

    @SuppressWarnings("SameParameterValue")
    protected GameLoggingLevels(String name, int value) {
        super(name, value);
    }

    protected GameLoggingLevels(String name, int value, String resourceBundleName) {
        super(name, value, resourceBundleName);
    }
}
