package com.qsoftware.bubbles.event;

import com.qsoftware.bubbles.QBubbles;

public class GameExitEvent extends Event {
    private final QBubbles game;

    public GameExitEvent(QBubbles game) {
        this.game = game;
    }

    public QBubbles getGame() {
        return game;
    }
}
