package com.ultreon.bubbles.command;

import com.ultreon.bubbles.command.tabcomplete.TabCompleter;
import com.ultreon.bubbles.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ScoreCommand implements TabExecutor {
    @Override
    public boolean execute(PlayerEntity player, String[] args) {
        if (args.length == 2) {
            long value;
            switch (args[0]) {
                case "add":
                    try {
                        value = Long.parseLong(args[1]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’");
                        return false;
                    }
                    player.addScore(value);
                    return true;
                case "set":
                    try {
                        value = Long.parseLong(args[1]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’");
                        return false;
                    }
                    player.setScore(value);
                    return true;
                case "subtract":
                    try {
                        value = Long.parseLong(args[1]);
                    } catch (NumberFormatException exception) {
                        player.sendMessage("Invalid number: ‘" + args[1] + "’");
                        return false;
                    }
                    player.subtractScore(value);
                    return true;
            }
        }

        player.sendMessage("Usage: /score <add|set|subtract> <value>");

        return false;
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return TabCompleter.getStrings(args[0], "add", "set", "subtract");
        } else if (args.length == 2) {
            return TabCompleter.getInts(args[1]);
        }

        return new ArrayList<>();
    }
}
