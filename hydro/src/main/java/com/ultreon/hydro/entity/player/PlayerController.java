package com.ultreon.hydro.entity.player;

import com.ultreon.hydro.Game;
import com.ultreon.hydro.input.KeyboardController;

import java.awt.event.KeyEvent;

public class PlayerController {
    private final Game game;

    public PlayerController() {
        this.game = Game.getInstance();
    }

    public void tick() {
        KeyboardController keyboard = KeyboardController.instance();

        IPlayer player = game.iPlayer;
        if (player != null) {
            player.forward(keyboard.isPressed(KeyEvent.VK_UP) || keyboard.isPressed(KeyEvent.VK_KP_UP) || keyboard.isPressed(KeyEvent.VK_W));
            player.backward(keyboard.isPressed(KeyEvent.VK_DOWN) || keyboard.isPressed(KeyEvent.VK_KP_DOWN) || keyboard.isPressed(KeyEvent.VK_S));
            player.right(keyboard.isPressed(KeyEvent.VK_RIGHT) || keyboard.isPressed(KeyEvent.VK_KP_RIGHT) || keyboard.isPressed(KeyEvent.VK_D));
            player.left(keyboard.isPressed(KeyEvent.VK_LEFT) || keyboard.isPressed(KeyEvent.VK_KP_LEFT) || keyboard.isPressed(KeyEvent.VK_A));
        }
    }
}
