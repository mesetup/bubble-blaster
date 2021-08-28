package com.qtech.bubbles.command;

import com.qtech.bubbles.entity.player.PlayerEntity;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        player.setDamageValue(player.getMaxDamageValue());
        return true;
    }
}
