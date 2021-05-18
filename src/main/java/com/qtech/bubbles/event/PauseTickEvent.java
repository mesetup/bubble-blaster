package com.qtech.bubbles.event;

import com.google.common.eventbus.EventBus;
import com.qtech.bubbles.QBubbles;

public class PauseTickEvent extends Event {
    private final QBubbles main;
    private final double deltaTime;

    public PauseTickEvent(QBubbles main, double deltaTime) {
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
