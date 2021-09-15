package com.ultreon.commons.lang;

/**
 * @author Qboi
 * @since 1.0.0
 */
public class LoggableProgress extends Progress {
    private final InfoTransporter infoTransporter;

    public LoggableProgress(InfoTransporter infoTransporter, int progress, int max) {
        super(progress, max);
        this.infoTransporter = infoTransporter;
    }

    public LoggableProgress(InfoTransporter infoTransporter, int max) {
        super(max);
        this.infoTransporter = infoTransporter;
    }

    public void log(String text) {
        infoTransporter.log(text);
    }
}
