package com.qsoftware.bubbles.common.command;

import com.qsoftware.bubbles.entity.player.PlayerEntity;

import java.util.HashMap;

public final class CommandConstructor {
    protected static HashMap<String, CommandExecutor> commands = new HashMap<>();

    public static void add(String name, CommandExecutor handler) {
        commands.put(name, handler);
    }

    public static CommandExecutor get(String name) {
        if (commands.containsKey(name)) {
            return commands.get(name);
        }

        return null;
    }

    public static boolean execute(String name, PlayerEntity player, String[] args) {
        CommandExecutor handler = get(name);

        if (handler == null) {
            return false;
        }

        try {
            handler.execute(new Command(name), player, args);
        } catch (Throwable throwable) {
            player.sendMessage("Error occurred when executing the command.");
            player.sendMessage("  See the log for more information.");
            throwable.printStackTrace();
            return false;
        }
        return true;
    }
}
