package com.qtech.bubbleblaster.common.command;

import com.qtech.bubbleblaster.entity.player.PlayerEntity;

public interface CommandExecutor {
    @SuppressWarnings("UnusedReturnValue")
    boolean execute(PlayerEntity player, String[] args);
}
