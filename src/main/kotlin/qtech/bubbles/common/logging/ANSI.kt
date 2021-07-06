@file:Suppress("unused")

package qtech.bubbles.common.logging

object ANSI {
    //    public static String startCode = "\u001B";
    //    public static String startCode = "\033";
    const val START_CODE = "^<ESC^>"
    const val ANSI_RESET = "$START_CODE[0m"
    const val ANSI_BLACK = "$START_CODE[30m"
    const val ANSI_RED = "$START_CODE[31m"
    const val ANSI_GREEN = "$START_CODE[32m"
    const val ANSI_YELLOW = "$START_CODE[33m"
    const val ANSI_BLUE = "$START_CODE[34m"
    const val ANSI_PURPLE = "$START_CODE[35m"
    const val ANSI_CYAN = "$START_CODE[36m"
    const val ANSI_WHITE = "$START_CODE[37m"
    const val ANSI_BRIGHT_BLACK = "$START_CODE[90m"
    const val ANSI_BRIGHT_RED = "$START_CODE[91m"
    const val ANSI_BRIGHT_GREEN = "$START_CODE[92m"
    const val ANSI_BRIGHT_YELLOW = "$START_CODE[93m"
    const val ANSI_BRIGHT_BLUE = "$START_CODE[94m"
    const val ANSI_BRIGHT_PURPLE = "$START_CODE[95m"
    const val ANSI_BRIGHT_CYAN = "$START_CODE[96m"
    const val ANSI_BRIGHT_WHITE = "$START_CODE[97m"
}