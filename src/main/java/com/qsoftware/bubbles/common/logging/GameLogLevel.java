package com.qsoftware.bubbles.common.logging;

import org.fusesource.jansi.Ansi;

import java.io.PrintStream;

public enum GameLogLevel {
    DEBUG("debug", System.err, Ansi.Color.CYAN),
    INFO("info", System.out, Ansi.Color.WHITE),
    SUCCESS("success", System.out, Ansi.Color.GREEN),
    WARN("warn", System.err, Ansi.Color.YELLOW),
    ERROR("error", System.err, Ansi.Color.RED),
    FATAL("fatal", System.err, Ansi.Color.RED);

    private final String name;
    private final PrintStream stream;
    private final Ansi.Color ansiColor;

    GameLogLevel(String name, PrintStream stream, Ansi.Color ansiColor) {
        this.name = name;
        this.stream = stream;
        this.ansiColor = ansiColor;
    }

    public String getName() {
        return name;
    }

    public PrintStream getStream() {
        return stream;
    }

    public Ansi.Color getAnsiColor() {
        return ansiColor;
    }
}
