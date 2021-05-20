package com.qtech.bubbleblaster.util.system;

public class Path {
    public static char getSeperator() {
        return System.getProperty("path.separator").charAt(0);
    }
}
