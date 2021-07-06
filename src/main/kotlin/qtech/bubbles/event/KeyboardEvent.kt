package qtech.bubbles.event

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.core.controllers.KeyboardController
import qtech.bubbles.event.type.KeyEventType
import java.awt.event.KeyEvent

/**
 * <h1>Keyboard Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see KeyboardEvent
 *
 * @see KeyboardController
 *
 * @see KeyEvent
 *
 * @see KeyEventType
 */
class KeyboardEvent(
    /**
     * Returns the Main instance used in the event.
     *
     * @return The Main instance.
     */
    val main: BubbleBlaster,
    /**
     * Returns the KeyboardController instance used in the event.
     *
     * @return The KeyboardController instance.
     */
    val controller: KeyboardController, val parentEvent: KeyEvent, val type: KeyEventType
) : Event() {
    val extendedKeyCode: Int = parentEvent.extendedKeyCode
    val keyCode: Int = parentEvent.keyCode
    val keyChar: Char = parentEvent.keyChar
    val modifiers: Int = parentEvent.modifiersEx
    val keyLocation: Int = parentEvent.keyLocation
    val `when`: Long = parentEvent.getWhen()
    val pressed = HashMap<Int, Boolean>()

}