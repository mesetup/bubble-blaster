package com.qsoftware.bubbles.common.interfaces;

public interface Listener {
//    boolean eventsActive = false;

    void bindEvents();

    void unbindEvents();

    boolean eventsAreActive();
}
