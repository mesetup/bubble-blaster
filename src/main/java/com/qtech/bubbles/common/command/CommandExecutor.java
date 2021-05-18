package com.qtech.bubbles.common.command;

import com.qtech.bubbles.entity.player.PlayerEntity;

public interface CommandExecutor {
    @SuppressWarnings("UnusedReturnValue")
    boolean execute(PlayerEntity player, String[] args);
}
