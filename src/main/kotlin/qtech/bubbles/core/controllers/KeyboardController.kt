package qtech.bubbles.core.controllers

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.event.KeyboardEvent
import qtech.bubbles.event.type.KeyEventType
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

/**
 * @see MouseController
 *
 * @see java.awt.event.KeyAdapter
 */
class KeyboardController : KeyAdapter() {
    private val pressed = HashSet<Int>()
    @Synchronized
    fun isPressed(keyCode: Int): Boolean {
        return pressed.contains(keyCode)
    }

    @Synchronized
    override fun keyPressed(e: KeyEvent) {
        if (isPressed(e.keyCode)) {
            BubbleBlaster.eventBus.post(KeyboardEvent(BubbleBlaster.instance, this, e, KeyEventType.HOLD))
        } else {
            pressed.add(e.keyCode)
            BubbleBlaster.eventBus.post(KeyboardEvent(BubbleBlaster.instance, this, e, KeyEventType.PRESS))
        }
    }

    @Synchronized
    override fun keyReleased(e: KeyEvent) {
        pressed.remove(e.keyCode)
        BubbleBlaster.eventBus.post(KeyboardEvent(BubbleBlaster.instance, this, e, KeyEventType.RELEASE))
    }

    @Synchronized
    override fun keyTyped(e: KeyEvent) {
        BubbleBlaster.eventBus.post(KeyboardEvent(BubbleBlaster.instance, this, e, KeyEventType.TYPE))
    }

    companion object {
        private val INSTANCE = KeyboardController()
        @JvmStatic
        fun instance(): KeyboardController {
            return INSTANCE
        }
    }
}