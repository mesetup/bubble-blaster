package com.qtech.bubbleblaster.common;

import com.qtech.bubbleblaster.BubbleBlaster;

import java.io.File;

public class References {
    // Dirs
    public static final File QBUBBLES_DIR = BubbleBlaster.getGameDir();
    public static final File LOGS_DIR = new File(QBUBBLES_DIR.getAbsolutePath(), "Logs");
    public static final File ADDONS_DIR = new File(QBUBBLES_DIR.getAbsolutePath(), "Addons");
    //    public static final String SAVES_DIR = Path.join(QBUBBLES_DIR, "saves");
    public static final File SAVES_DIR = new File(QBUBBLES_DIR, "Saves");

    // Files
    public static final File SETTINGS_FILE = new File(QBUBBLES_DIR, "Settings.json");
    public static final File CRASH_REPORTS = new File(QBUBBLES_DIR, "CrashReports");

    static {
        //noinspection ResultOfMethodCallIgnored
        LOGS_DIR.mkdirs();
    }

    // Version
    public static final Version VERSION = new Version(1, 0, 0, VersionType.ALPHA, 0);
}
