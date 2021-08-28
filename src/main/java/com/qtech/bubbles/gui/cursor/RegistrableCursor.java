package com.qtech.bubbles.gui.cursor;

import com.qtech.bubbles.registry.RegistryEntry;

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
