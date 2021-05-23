package com.qtech.bubbleblaster.event;

import com.qtech.bubbleblaster.BubbleBlaster;

public class GameExitEvent extends Event {
    private final BubbleBlaster game;

    public GameExitEvent(BubbleBlaster game) {
        this.game = game;
    }

    public BubbleBlaster getGame() {
        return game;
    }
}
