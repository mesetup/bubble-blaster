package com.ultreon.commons.lang;

import java.util.function.Consumer;

/**
 * @author Qboi
 * @since 1.0.0
 */
public class InfoTransporter implements ILogger {
    private final Consumer<String> onLog;

    public InfoTransporter(Consumer<String> consumer) {
        this.onLog = consumer;
    }

    public void log(String text) {
        this.onLog.accept(text);
    }
}
