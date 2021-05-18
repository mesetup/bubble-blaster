package com.qtech.bubbles.command;

import com.qtech.bubbles.common.command.CommandExecutor;
import com.qtech.bubbles.entity.player.PlayerEntity;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        player.setHealth(player.getMaxHealth());
        return true;
    }
}
