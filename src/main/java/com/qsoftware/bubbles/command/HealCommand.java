package com.qsoftware.bubbles.command;

import com.qsoftware.bubbles.common.command.Command;
import com.qsoftware.bubbles.common.command.CommandExecutor;
import com.qsoftware.bubbles.entity.player.PlayerEntity;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean execute(Command command, PlayerEntity player, String[] args) {
        player.setHealth(player.getMaxHealth());
        return true;
    }
}
