package com.qtech.bubbles.common.cursor;

import com.qtech.bubbles.common.RegistryEntry;

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
