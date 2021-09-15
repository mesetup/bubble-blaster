package com.ultreon.bubbles.command;

import com.ultreon.bubbles.entity.player.PlayerEntity;

public interface CommandExecutor {
    @SuppressWarnings("UnusedReturnValue")
    boolean execute(PlayerEntity player, String[] args);
}
