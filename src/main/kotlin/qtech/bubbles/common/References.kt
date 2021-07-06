package qtech.bubbles.common

import qtech.bubbles.BubbleBlaster
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object References {
    // Dirs
    val QBUBBLES_DIR: File = BubbleBlaster.gameDir
    val LOGS_DIR: File = File(QBUBBLES_DIR.absolutePath, "Logs")
    val ADDONS_DIR: File = File(QBUBBLES_DIR.absolutePath, "Mods")

    val SAVES_DIR_PATH: Path = Paths.get(QBUBBLES_DIR.toString(), "saves");
    val SAVES_DIR: File = File(QBUBBLES_DIR, "Saves")

    // Files
    val SETTINGS_FILE: File = File(QBUBBLES_DIR, "Settings.json")
    val CRASH_REPORTS: File = File(QBUBBLES_DIR, "CrashReports")

    // Version
    val VERSION: Version = Version(1, 0, 0, VersionType.ALPHA, 0)

    init {
        LOGS_DIR.mkdirs()
    }
}