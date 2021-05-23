package com.qtech.bubbleblaster.common.cursor;

import com.qtech.bubbleblaster.common.RegistryEntry;


public class RegistrableCursor extends RegistryEntry {
    private final Cursor cursor;

    public RegistrableCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
