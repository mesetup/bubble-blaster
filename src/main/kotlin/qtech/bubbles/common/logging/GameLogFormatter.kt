package qtech.bubbles.common.logging

import org.fusesource.jansi.Ansi
import java.io.PrintWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GameLogFormatter {
    fun publish(record: GameLogRecord, printer: PrintWriter?) {
        val date = Date(record.milliTime)
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = formatter.format(date)
        SimpleDateFormat.getDateTimeInstance()
        record.level.stream.println(Ansi.ansi().fg(record.level.ansiColor).a(dateFormatted + " | " + record.loggerName + " | " + record.level.text.uppercase(Locale.getDefault()) + ": " + record.message).reset())
        record.level.stream.flush()
        printer!!.println(dateFormatted + " | " + record.loggerName + " | " + record.level.text.uppercase(Locale.getDefault()) + ": " + record.message)
        printer.flush()
    }
}