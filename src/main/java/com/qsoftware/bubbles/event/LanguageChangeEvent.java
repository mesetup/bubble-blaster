package com.qsoftware.bubbles.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class LanguageChangeEvent {
    private final Locale from;
    private final Locale to;

    public LanguageChangeEvent(Locale from, Locale to) {
        this.from = from;
        this.to = to;
    }

    @Nullable
    public Locale getFrom() {
        return from;
    }

    @NotNull
    public Locale getTo() {
        return to;
    }
}
