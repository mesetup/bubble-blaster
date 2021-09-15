package com.ultreon.bubbles.util;

public class ExceptionUtils {
    private ExceptionUtils() {
        throw ExceptionUtils.utilityClass();
    }

    public static IllegalAccessError utilityClass() {
        return new IllegalAccessError("Tried to initialize utility class.");
    }
}
