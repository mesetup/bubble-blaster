package qtech.bubbles.common.logging

import org.fusesource.jansi.Ansi
import java.io.PrintStream

enum class GameLogLevel(val text: String, val stream: PrintStream, val ansiColor: Ansi.Color) {
    DEBUG("debug", System.err, Ansi.Color.CYAN),
    INFO("info", System.out, Ansi.Color.WHITE),
    SUCCESS("success", System.out, Ansi.Color.GREEN),
    WARN("warn", System.err, Ansi.Color.YELLOW),
    ERROR("error", System.err, Ansi.Color.RED),
    FATAL("fatal", System.err, Ansi.Color.RED);

}