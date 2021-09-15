package com.ultreon.bubbles.common.logging.old;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class GameLoggingFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return "[" + record.getLoggerName() + "] " + record.getLevel().getName() + ": " + record.getMessage() + "\n";
    }
}
