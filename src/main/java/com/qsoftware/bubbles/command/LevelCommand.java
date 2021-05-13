package com.qsoftware.bubbles.command;

import com.qsoftware.bubbles.common.command.Command;
import com.qsoftware.bubbles.common.command.CommandExecutor;
import com.qsoftware.bubbles.entity.player.PlayerEntity;

public class LevelCommand implements CommandExecutor {
    @Override
    public boolean execute(Command command, PlayerEntity player, String[] args) {
        if (args.length == 2) {
            int value;
            switch (args[0]) {
                case "add":
                    try {
                        value = Integer.parseInt(args[1]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’");
                        return false;
                    }
                    player.setLevel(player.getLevel() + value);
                    return true;
                case "set":
                    try {
                        value = Integer.parseInt(args[1]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’");
                        return false;
                    }
                    player.setLevel(value);
                    return true;
                case "subtract":
                    try {
                        value = Integer.parseInt(args[1]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’");
                        return false;
                    }
                    player.setLevel(player.getLevel() - value);
                    return true;
            }
        } else if (args.length == 1) {
            switch (args[0]) {
                case "up":
                    player.levelUp();
                    return true;
                case "down":
                    player.levelDown();
                    return true;
            }
        }

        player.sendMessage("Usage: /level <up|down>");
        player.sendMessage("Usage: /level <add|set|subtract> <value>");

        return false;
    }
}
