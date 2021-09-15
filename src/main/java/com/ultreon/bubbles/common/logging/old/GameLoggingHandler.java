package com.ultreon.bubbles.common.logging.old;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class GameLoggingHandler extends Handler {
    @Override
    public void publish(LogRecord record) {
        Date date = new Date(record.getMillis());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS XXX");
        formatter.setTimeZone(TimeZone.getDefault());
        String dateFormatted = formatter.format(date);

        if (record.getLevel() == GameLoggingLevels.DEBUG || record.getLevel() == Level.WARNING || record.getLevel() == Level.SEVERE) {
            System.err.println(dateFormatted + " | " + record.getLoggerName() + " | " + record.getLevel().getName() + ": " + record.getMessage());
        } else {
            System.out.println(dateFormatted + " | " + record.getLoggerName() + " | " + record.getLevel().getName() + ": " + record.getMessage());
        }
    }

    @Override
    public void flush() {
        System.out.flush();
    }

    @Override
    public void close() throws SecurityException {
        System.out.close();
    }
}
