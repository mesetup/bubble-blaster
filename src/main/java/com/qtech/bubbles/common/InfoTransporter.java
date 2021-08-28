package com.qtech.bubbles.common;

import java.util.function.Consumer;

/**
 * @author Quinten
 * @since 1.0.0
 */
public class InfoTransporter {
    private final Consumer<String> onLog;

    public InfoTransporter(Consumer<String> consumer) {
        this.onLog = consumer;
    }

    public void log(String text) {
        this.onLog.accept(text);
    }
}
