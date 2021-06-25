package com.qtech.bubbles.command;

import com.qtech.bubbles.common.command.CommandExecutor;
import com.qtech.bubbles.entity.player.PlayerEntity;

public class GameOverCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        player.getGameType().triggerGameOver();
        return true;
    }
}
