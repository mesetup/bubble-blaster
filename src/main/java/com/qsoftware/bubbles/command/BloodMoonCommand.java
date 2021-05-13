package com.qsoftware.bubbles.command;

import com.qsoftware.bubbles.common.command.Command;
import com.qsoftware.bubbles.common.command.CommandExecutor;
import com.qsoftware.bubbles.entity.player.PlayerEntity;

public class BloodMoonCommand implements CommandExecutor {
    @Override
    public boolean execute(Command command, PlayerEntity player, String[] args) {
        player.getGameType().triggerBloodMoon();
        return true;
    }
}
