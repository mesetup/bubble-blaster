package com.qtech.bubbleblaster.common.logging;

import com.qtech.bubbleblaster.common.References;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class GameLogger {
    private final Logger logger;
    private static final ConcurrentHashMap<String, GameLogger> loggers = new ConcurrentHashMap<>();
    private final GameLogFormatter formatter = new GameLogFormatter();
    private String name;

    public void debug(String msg) {
        this.log(GameLogLevel.DEBUG, msg);
    }

    public void info(String msg) {
        this.log(GameLogLevel.INFO, msg);
    }

    public void success(String msg) {
        this.log(GameLogLevel.SUCCESS, msg);
    }

    public void error(String msg) {
        this.log(GameLogLevel.ERROR, msg);
    }

    public void warning(String msg) {
        this.log(GameLogLevel.WARN, msg);
    }

    public void fatal(String msg) {
        this.log(GameLogLevel.FATAL, msg);
    }

    private void log(GameLogLevel level, String msg) {
        GameLogRecord record = new GameLogRecord(msg, this, level);
        this.formatter.publish(record, printer);
    }

    private static PrintWriter printer = null;

    static {
        try {
            Date date = new Date(System.currentTimeMillis());
            DateFormat formatter = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String logFileName = formatter.format(date) + ".log";

            FileOutputStream output = new FileOutputStream(new File(References.LOGS_DIR, logFileName), true);
            printer = new PrintWriter(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameLogger(String name) throws InstanceAlreadyExistsException {
        if (loggers.containsKey(name))
            throw new InstanceAlreadyExistsException("GameLogger instance with name '" + name + "' already created");

        // Get Java logger.
        this.logger = Logger.getLogger(name);
        this.name = name;

        // Register logger.
        loggers.put(name, this);
    }

    public static ConcurrentHashMap<String, GameLogger> getLoggers() {
        return loggers;
    }

    public static GameLogger getLoggerInstance(String name) {
        return loggers.get(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Logger getLogger() {
        return logger;
    }
}
