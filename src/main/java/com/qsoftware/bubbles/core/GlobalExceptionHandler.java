package com.qsoftware.bubbles.core;

import com.qsoftware.bubbles.QBubbles;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static Logger logger = QBubbles.getLogger();

    static {
//        try {
//            logger = new GameLogger("STDERR");
//        } catch (InstanceAlreadyExistsException e) {
//            e.printStackTrace();
//        }
    }

    private static boolean hooked = false;

    public void uncaughtException(Thread t, Throwable e) {
//        logger.fatal("Unhandled exception caught!");
        printStackTrace(e);
        System.exit(1);
    }

    /**
     * Message for trying to suppress a null exception.
     */
    private static final String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";

    /**
     * Message for trying to suppress oneself.
     */
    private static final String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";

    /**
     * Caption  for labeling causative exception stack traces
     */
    private static final String CAUSE_CAPTION = "Caused by: ";

    /**
     * Caption for labeling suppressed exception stack traces
     */
    private static final String SUPPRESSED_CAPTION = "Suppressed: ";

    private void printStackTrace(Throwable e) {
        PrintStreamOrWriter s = new WrappedPrintStream(System.err);


        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
        dejaVu.add(e);

        synchronized (s.lock()) {
            // Print our stack trace
            logger.fatal(e.toString());
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement traceElement : trace)
                logger.fatal("\tat " + traceElement);

            // Print suppressed exceptions, if any
            for (Throwable se : e.getSuppressed())
                printEnclosedStackTrace(se, s, trace, SUPPRESSED_CAPTION, "\t", dejaVu);

            // Print cause, if any
            Throwable ourCause = e.getCause();
            if (ourCause != null)
                printEnclosedStackTrace(ourCause, s, trace, CAUSE_CAPTION, "", dejaVu);
        }
    }

    /**
     * Print our stack trace as an enclosed exception for the specified
     * stack trace.
     */
    private void printEnclosedStackTrace(Throwable e, PrintStreamOrWriter s,
                                         StackTraceElement[] enclosingTrace,
                                         String caption,
                                         String prefix,
                                         Set<Throwable> dejaVu) {
        assert Thread.holdsLock(s.lock());
        if (dejaVu.contains(e)) {
            s.println(prefix + caption + "[CIRCULAR REFERENCE: " + this + "]");
        } else {
            dejaVu.add(e);
            // Compute number of frames in common between this and enclosing trace
            StackTraceElement[] trace = e.getStackTrace();
            int m = trace.length - 1;
            int n = enclosingTrace.length - 1;
            while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                m--;
                n--;
            }
            int framesInCommon = trace.length - 1 - m;

            // Print our stack trace
            s.println(prefix + caption + this);
            for (int i = 0; i <= m; i++)
                s.println(prefix + "\tat " + trace[i]);
            if (framesInCommon != 0)
                s.println(prefix + "\t... " + framesInCommon + " more");

            // Print suppressed exceptions, if any
            for (Throwable se : e.getSuppressed())
                printEnclosedStackTrace(se, s, trace, SUPPRESSED_CAPTION,
                        prefix + "\t", dejaVu);

            // Print cause, if any
            Throwable ourCause = e.getCause();
            if (ourCause != null)
                printEnclosedStackTrace(ourCause, s, trace, CAUSE_CAPTION, prefix, dejaVu);
        }
    }

    public static void setLogger(Logger logger) throws IllegalStateException {
        if (hooked) throw new IllegalStateException("Not allowed to access after hooked!");
        hooked = true;
        GlobalExceptionHandler.logger = logger;
    }

    /**
     * Wrapper class for PrintStream and PrintWriter to enable a single
     * implementation of printStackTrace.
     */
    private abstract static class PrintStreamOrWriter {
        /**
         * Returns the object to be locked when using this StreamOrWriter
         */
        abstract Object lock();

        /**
         * Prints the specified string as a line on this StreamOrWriter
         */
        abstract void println(Object o);
    }

    private static class WrappedPrintStream extends PrintStreamOrWriter {
        private final PrintStream printStream;

        WrappedPrintStream(PrintStream printStream) {
            this.printStream = printStream;
        }

        Object lock() {
            return printStream;
        }

        void println(Object o) {
            logger.fatal(o.toString());
        }
    }

    private static class WrappedPrintWriter extends PrintStreamOrWriter {
        private final PrintWriter printWriter;

        WrappedPrintWriter(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        Object lock() {
            return printWriter;
        }

        void println(Object o) {
            logger.fatal(o.toString());
        }
    }
}
