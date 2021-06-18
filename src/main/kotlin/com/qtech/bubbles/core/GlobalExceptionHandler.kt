package com.qtech.bubbles.core

import com.qtech.bubbles.BubbleBlaster
import org.apache.logging.log4j.Logger
import java.io.PrintStream
import java.io.PrintWriter
import java.util.*

class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
//        logger.fatal("Unhandled exception caught!");
        printStackTrace(e)
        System.exit(1)
    }

    private fun printStackTrace(e: Throwable) {
        val s: PrintStreamOrWriter = WrappedPrintStream(System.err)


        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        val dejaVu = Collections.newSetFromMap(IdentityHashMap<Throwable, Boolean>())
        dejaVu.add(e)
        synchronized(s.lock()) {

            // Print our stack trace
            logger.fatal(e.toString())
            val trace = e.stackTrace
            for (traceElement in trace) logger.fatal("\tat $traceElement")

            // Print suppressed exceptions, if any
            for (se in e.suppressed) printEnclosedStackTrace(se, s, trace, SUPPRESSED_CAPTION, "\t", dejaVu)

            // Print cause, if any
            val ourCause = e.cause
            if (ourCause != null) printEnclosedStackTrace(ourCause, s, trace, CAUSE_CAPTION, "", dejaVu)
        }
    }

    /**
     * Print our stack trace as an enclosed exception for the specified
     * stack trace.
     */
    private fun printEnclosedStackTrace(
        e: Throwable, s: PrintStreamOrWriter,
        enclosingTrace: Array<StackTraceElement>,
        caption: String,
        prefix: String,
        dejaVu: MutableSet<Throwable>
    ) {
        assert(Thread.holdsLock(s.lock()))
        if (dejaVu.contains(e)) {
            s.println("$prefix$caption[CIRCULAR REFERENCE: $this]")
        } else {
            dejaVu.add(e)
            // Compute number of frames in common between this and enclosing trace
            val trace = e.stackTrace
            var m = trace.size - 1
            var n = enclosingTrace.size - 1
            while (m >= 0 && n >= 0 && trace[m] == enclosingTrace[n]) {
                m--
                n--
            }
            val framesInCommon = trace.size - 1 - m

            // Print our stack trace
            s.println(prefix + caption + this)
            for (i in 0..m) s.println(prefix + "\tat " + trace[i])
            if (framesInCommon != 0) s.println("$prefix\t... $framesInCommon more")

            // Print suppressed exceptions, if any
            for (se in e.suppressed) printEnclosedStackTrace(
                se, s, trace, SUPPRESSED_CAPTION,
                prefix + "\t", dejaVu
            )

            // Print cause, if any
            val ourCause = e.cause
            if (ourCause != null) printEnclosedStackTrace(ourCause, s, trace, CAUSE_CAPTION, prefix, dejaVu)
        }
    }

    /**
     * Wrapper class for PrintStream and PrintWriter to enable a single
     * implementation of printStackTrace.
     */
    private abstract class PrintStreamOrWriter {
        /**
         * Returns the object to be locked when using this StreamOrWriter
         */
        abstract fun lock(): Any

        /**
         * Prints the specified string as a line on this StreamOrWriter
         */
        abstract fun println(o: Any)
    }

    private class WrappedPrintStream(private val printStream: PrintStream) : PrintStreamOrWriter() {
        override fun lock(): Any {
            return printStream
        }

        override fun println(o: Any) {
            logger.fatal(o.toString())
        }
    }

    private class WrappedPrintWriter(private val printWriter: PrintWriter) : PrintStreamOrWriter() {
        override fun lock(): Any {
            return printWriter
        }

        override fun println(o: Any) {
            logger.fatal(o.toString())
        }
    }

    companion object {
        private var logger = BubbleBlaster.logger
        private var hooked = false

        /**
         * Message for trying to suppress a null exception.
         */
        private const val NULL_CAUSE_MESSAGE = "Cannot suppress a null exception."

        /**
         * Message for trying to suppress oneself.
         */
        private const val SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted"

        /**
         * Caption  for labeling causative exception stack traces
         */
        private const val CAUSE_CAPTION = "Caused by: "

        /**
         * Caption for labeling suppressed exception stack traces
         */
        private const val SUPPRESSED_CAPTION = "Suppressed: "
        @Throws(IllegalStateException::class)
        fun setLogger(logger: Logger) {
            check(!hooked) { "Not allowed to access after hooked!" }
            hooked = true
            Companion.logger = logger
        }
    }
}