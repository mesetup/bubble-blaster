package com.qsoftware.bubbles.common.cursor;

import com.qsoftware.bubbles.common.RegistryEntry;

import java.awt.*;

public class RegistrableCursor extends RegistryEntry {
    private final Cursor cursor;

    public RegistrableCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
