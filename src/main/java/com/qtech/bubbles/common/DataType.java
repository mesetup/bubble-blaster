package com.qtech.bubbles.common;

import org.jetbrains.annotations.Nullable;

public enum DataType {
    ASSETS("assets"),
    DATA("data"),
    META_INFO("META_INF"),
    OBJECT(null);

    @Nullable
    private final String path;

    DataType(@Nullable String path) {
        this.path = path;
    }

    @Nullable
    public String getPath() {
        return path;
    }
}
