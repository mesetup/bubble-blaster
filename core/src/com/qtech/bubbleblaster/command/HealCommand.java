package com.qtech.bubbleblaster.command;

import com.qtech.bubbleblaster.common.command.CommandExecutor;
import com.qtech.bubbleblaster.entity.player.PlayerEntity;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        player.setDamageValue(player.getMaxDamageValue());
        return true;
    }
}