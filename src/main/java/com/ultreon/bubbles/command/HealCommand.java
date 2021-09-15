package com.ultreon.bubbles.command;

import com.ultreon.bubbles.entity.player.PlayerEntity;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        player.setDamageValue(player.getMaxDamageValue());
        return true;
    }
}
