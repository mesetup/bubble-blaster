package com.qtech.bubbleblaster.entity.player;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.gametype.AbstractGameType;
import com.qtech.bubbleblaster.core.controllers.KeyboardController;
import com.qtech.bubbleblaster.environment.Environment;


public class PlayerController {
    public PlayerController() {

    }

    public void tick() {
        KeyboardController keyboard = KeyboardController.instance();
        Environment environment = BubbleBlaster.getInstance().environment;

        if (environment != null) {
            AbstractGameType gameType = environment.getGameType();
            if (gameType != null) {
                PlayerEntity player = gameType.getPlayer();
                if (player != null) {
                    player.forward(keyboard.isPressed(KeyEvent.VK_UP) || keyboard.isPressed(KeyEvent.VK_KP_UP) || keyboard.isPressed(KeyEvent.VK_W));
                    player.backward(keyboard.isPressed(KeyEvent.VK_DOWN) || keyboard.isPressed(KeyEvent.VK_KP_DOWN) || keyboard.isPressed(KeyEvent.VK_S));
                    player.right(keyboard.isPressed(KeyEvent.VK_RIGHT) || keyboard.isPressed(KeyEvent.VK_KP_RIGHT) || keyboard.isPressed(KeyEvent.VK_D));
                    player.left(keyboard.isPressed(KeyEvent.VK_LEFT) || keyboard.isPressed(KeyEvent.VK_KP_LEFT) || keyboard.isPressed(KeyEvent.VK_A));
                }
            }
        }
    }
}
