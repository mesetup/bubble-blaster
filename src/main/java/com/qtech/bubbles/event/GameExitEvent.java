package com.qtech.bubbles.event;

import com.qtech.bubbles.BubbleBlaster;

public class GameExitEvent extends Event {
    private final BubbleBlaster game;

    public GameExitEvent(BubbleBlaster game) {
        this.game = game;
    }

    public BubbleBlaster getGame() {
        return game;
    }
}
