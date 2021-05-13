package com.qsoftware.bubbles.util.system;

public class Path {
    public static char getSeperator() {
        return System.getProperty("path.separator").charAt(0);
    }
}
