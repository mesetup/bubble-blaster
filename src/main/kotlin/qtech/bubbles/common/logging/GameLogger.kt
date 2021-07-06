@file:Suppress("unused")

package qtech.bubbles.common.logging

import qtech.bubbles.common.References
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger
import javax.management.InstanceAlreadyExistsException

class GameLogger(name: String) {
    val logger: Logger
    private val formatter = GameLogFormatter()
    var name: String
    fun debug(msg: String) {
        log(GameLogLevel.DEBUG, msg)
    }

    fun info(msg: String) {
        log(GameLogLevel.INFO, msg)
    }

    fun success(msg: String) {
        log(GameLogLevel.SUCCESS, msg)
    }

    fun error(msg: String) {
        log(GameLogLevel.ERROR, msg)
    }

    fun warning(msg: String) {
        log(GameLogLevel.WARN, msg)
    }

    fun fatal(msg: String) {
        log(GameLogLevel.FATAL, msg)
    }

    private fun log(level: GameLogLevel, msg: String) {
        val record = GameLogRecord(msg, this, level)
        formatter.publish(record, printer)
    }

    companion object {
        val loggers = ConcurrentHashMap<String, GameLogger>()
        private var printer: PrintWriter? = null
        fun getLoggerInstance(name: String): GameLogger? {
            return loggers[name]
        }

        init {
            try {
                val date = Date(System.currentTimeMillis())
                val formatter: DateFormat = SimpleDateFormat("dd_MM_yyyy HH_mm_ss")
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val logFileName: String = formatter.format(date) + ".log"
                val output = FileOutputStream(File(References.LOGS_DIR, logFileName), true)
                printer = PrintWriter(output)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    init {
        if (loggers.containsKey(name)) throw InstanceAlreadyExistsException("GameLogger instance with name '$name' already created")

        // Get Java logger.
        logger = Logger.getLogger(name)
        this.name = name

        // Register logger.
        loggers[name] = this
    }
}