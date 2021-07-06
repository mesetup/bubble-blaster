package qtech.bubbles.common.logging.old

import java.util.logging.Formatter
import java.util.logging.LogRecord

class GameLoggingFormatter : Formatter() {
    override fun format(record: LogRecord): String {
        return """
            [${record.loggerName}] ${record.level.name}: ${record.message}
            
            """.trimIndent()
    }
}