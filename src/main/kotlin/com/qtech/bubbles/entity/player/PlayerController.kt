package com.qtech.bubbles.entity.player

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.core.controllers.KeyboardController.Companion.instance
import java.awt.event.KeyEvent

class PlayerController {
    fun tick() {
        val keyboard = instance()
        val environment = BubbleBlaster.instance.environment
        if (environment != null) {
            val gameType = environment.gameMode
            val player = gameType.player
            if (player != null) {
                player.forward(keyboard.isPressed(KeyEvent.VK_UP) || keyboard.isPressed(KeyEvent.VK_KP_UP) || keyboard.isPressed(KeyEvent.VK_W))
                player.backward(keyboard.isPressed(KeyEvent.VK_DOWN) || keyboard.isPressed(KeyEvent.VK_KP_DOWN) || keyboard.isPressed(KeyEvent.VK_S))
                player.right(keyboard.isPressed(KeyEvent.VK_RIGHT) || keyboard.isPressed(KeyEvent.VK_KP_RIGHT) || keyboard.isPressed(KeyEvent.VK_D))
                player.left(keyboard.isPressed(KeyEvent.VK_LEFT) || keyboard.isPressed(KeyEvent.VK_KP_LEFT) || keyboard.isPressed(KeyEvent.VK_A))
            }
        }
    }
}