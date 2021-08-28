package com.qtech.bubbles.common.text.translation;

public class Translation {
    private final String id;
    private final Object[] format;

    public Translation(String id, Object... format) {
        this.id = id;
        this.format = format;
    }

    @Override
    public String toString() {
        return I18n.translateToLocalFormatted(this.id, this.format);
    }

    public void set(int i, Object o) {
        this.format[i] = o;
    }

    public Object get(int i) {
        return this.format[i];
    }
}
