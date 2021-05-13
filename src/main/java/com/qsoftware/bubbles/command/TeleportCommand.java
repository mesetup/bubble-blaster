package com.qsoftware.bubbles.command;

import com.qsoftware.bubbles.common.command.Command;
import com.qsoftware.bubbles.common.command.CommandExecutor;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.entity.player.PlayerEntity;
import com.qsoftware.bubbles.scene.GameScene;
import com.qsoftware.bubbles.util.Util;

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean execute(Command command, PlayerEntity player, String[] args) {
        if (args.length == 2) {
            double x, y;

            try {
                x = Double.parseDouble(args[0]);
                y = Double.parseDouble(args[1]);
            } catch (NumberFormatException exception) {
                try {
                    x = Long.parseLong(args[0]);
                } catch (NumberFormatException exception1) {
                    player.sendMessage("Invalid number for x!");
                    return false;
                }
                try {
                    y = Long.parseLong(args[1]);
                } catch (NumberFormatException exception1) {
                    player.sendMessage("Invalid number for y!");
                    return false;
                }
            }

            Scene currentScene = Util.getSceneManager().getCurrentScene();
            if (!(currentScene instanceof GameScene)) return false;
            GameScene gameScene = (GameScene) currentScene;

            gameScene.getGameType().getPlayer().teleport(x, y);
            return true;
        }
        return false;
    }
}
