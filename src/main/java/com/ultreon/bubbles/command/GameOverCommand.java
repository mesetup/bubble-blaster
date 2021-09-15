package com.ultreon.bubbles.command;

import com.ultreon.bubbles.entity.player.PlayerEntity;

public class GameOverCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        player.getGameType().triggerGameOver();
        return true;
    }
}
