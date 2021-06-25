package com.qtech.bubbles.event;

import com.qtech.bubbles.QBubbles;

public class GameExitEvent extends Event {
    private final QBubbles game;

    public GameExitEvent(QBubbles game) {
        this.game = game;
    }

    public QBubbles getGame() {
        return game;
    }
}
