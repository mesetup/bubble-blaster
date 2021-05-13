package com.qsoftware.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.scene.Scene;

public class PauseTickEvent extends Event {
    private static EventBus eventBus;

    public static EventBus getEventBus() {
        return eventBus;
    }

    private final QBubbles main;
    private final double deltaTime;

    public PauseTickEvent(QBubbles main, Scene scene, double deltaTime) {
        this.main = main;
        this.deltaTime = deltaTime;
    }

    public QBubbles getMain() {
        return main;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
