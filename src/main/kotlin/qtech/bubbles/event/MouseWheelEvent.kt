package qtech.bubbles.event

import qtech.bubbles.BubbleBlaster
import qtech.bubbles.core.controllers.KeyboardController
import qtech.bubbles.core.controllers.MouseController
import qtech.bubbles.event.type.KeyEventType
import qtech.bubbles.event.type.MouseEventType
import java.awt.event.MouseEvent

/**
 * <h1>Mouse Wheel Event</h1>
 * This event is used for handling keyboard input.
 *
 * @see MouseMotionEvent
 *
 * @see qtech.bubbles.event.MouseEvent
 *
 * @see MouseController
 *
 * @see MouseEventType
 *
 * @see java.awt.event.MouseWheelEvent
 */
class MouseWheelEvent
/**
 * Keyboard event, called from a specific scene.
 *
 * @param main       The [BubbleBlaster] instance.
 * @param controller The [KeyboardController] instance.
 * @param event      The [KeyEvent] instance.
 * @param type       One of the [KeyEventType] constants.
 */(
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
    val controller: MouseController, val parentEvent: MouseEvent, val type: MouseEventType
) : Event()