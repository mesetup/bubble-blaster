package com.ultreon.bubbles.common;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.commons.lang.Version;
import com.ultreon.commons.lang.VersionType;

import java.io.File;

public class References {
    // Dirs
    public static final File GAME_DIR = BubbleBlaster.getGameDir();
    public static final File LOGS_DIR = new File(GAME_DIR.getAbsolutePath(), "Logs");
    public static final File MODS_DIR = new File(GAME_DIR.getAbsolutePath(), "Mods");
    public static final File SAVES_DIR = new File(GAME_DIR, "Saves");

    // Files
    public static final File SETTINGS_FILE = new File(GAME_DIR, "Settings.json");
    public static final File CRASH_REPORTS = new File(GAME_DIR, "CrashReports");

    static {
        //noinspection ResultOfMethodCallIgnored
        LOGS_DIR.mkdirs();
    }

    // Version
    public static final Version VERSION = new Version(1, 0, 0, VersionType.ALPHA, 0);
}
