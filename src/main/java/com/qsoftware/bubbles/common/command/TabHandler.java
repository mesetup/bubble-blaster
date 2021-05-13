package com.qsoftware.bubbles.common.command;

import com.qsoftware.bubbles.entity.player.PlayerEntity;

import java.util.List;

public interface TabHandler {
    List<String> tabComplete(Command command, PlayerEntity player, String[] args);
}
