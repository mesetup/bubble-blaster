package com.qtech.bubbles.event

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.core.controllers.KeyboardController
import com.qtech.bubbles.core.controllers.MouseController
import com.qtech.bubbles.event.type.KeyEventType
import com.qtech.bubbles.event.type.MouseEventType
import java.awt.event.MouseEvent

/**
 * <h1>Mouse Motion Event</h1>
 * This event is used for handling mouse motion input.
 *
 * @see com.qtech.bubbles.event.MouseEvent
 *
 * @see MouseMotionEvent
 *
 * @see MouseController
 *
 * @see MouseEventType
 *
 * @see java.awt.event.MouseEvent
 */
class MouseMotionEvent
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