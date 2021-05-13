package com.qsoftware.bubbles.common.command;

import com.qsoftware.bubbles.entity.player.PlayerEntity;

public interface CommandExecutor {
    boolean execute(Command command, PlayerEntity player, String[] args);
}
