package qtech.bubbles.common.logging.old

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord

class GameLoggingHandler : Handler() {
    override fun publish(record: LogRecord) {
        val date = Date(record.millis)
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS XXX")
        formatter.timeZone = TimeZone.getDefault()
        val dateFormatted = formatter.format(date)
        if (record.level === GameLoggingLevels.DEBUG || record.level === Level.WARNING || record.level === Level.SEVERE) {
            System.err.println(dateFormatted + " | " + record.loggerName + " | " + record.level.name + ": " + record.message)
        } else {
            println(dateFormatted + " | " + record.loggerName + " | " + record.level.name + ": " + record.message)
        }
    }

    override fun flush() {
        System.out.flush()
    }

    @Throws(SecurityException::class)
    override fun close() {
        System.out.close()
    }
}