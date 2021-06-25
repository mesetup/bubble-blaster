package com.qtech.bubbles.common.logging;

import org.fusesource.jansi.Ansi;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GameLogFormatter {
    public void publish(GameLogRecord record, PrintWriter printer) {
        Date date = new Date(record.getMilliTime());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);

        SimpleDateFormat.getDateTimeInstance();

        record.getLevel().getStream().println(Ansi.ansi().fg(record.getLevel().getAnsiColor()).a(dateFormatted + " | " + record.getLoggerName() + " | " + record.getLevel().getName().toUpperCase() + ": " + record.getMessage()).reset());
        record.getLevel().getStream().flush();
        printer.println(dateFormatted + " | " + record.getLoggerName() + " | " + record.getLevel().getName().toUpperCase() + ": " + record.getMessage());
        printer.flush();
    }
}
