package com.qtech.bubbles.event

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.core.controllers.MouseController
import com.qtech.bubbles.event.type.MouseEventType
import java.awt.Point
import java.awt.event.MouseEvent

/**
 * <h1>Mouse Event</h1>
 * This event is used for handling mouse input.
 *
 * @see MouseMotionEvent
 *
 * @see MouseWheelEvent
 *
 * @see MouseController
 *
 * @see MouseEventType
 *
 * @see java.awt.event.MouseEvent
 */
class MouseEvent(
    /**
     * Returns the Main instance used in the event.
     *
     * @return The Main instance.
     */
    val main: BubbleBlaster, controller: MouseController, event: MouseEvent, val type: MouseEventType
) : Event() {
    val button: Int
    val locationOnScreen: Point
    val clickCount: Int
    val point: Point
    val x: Int
    val xOnScreen: Int
    val y: Int
    val yOnScreen: Int

    /**
     * Returns the KeyboardController instance used in the event.
     *
     * @return The KeyboardController instance.
     */
    val controller: MouseController
    val parentEvent: MouseEvent

    /**
     * Keyboard event, called from a specific scene.
     *
     * @param main       The [BubbleBlaster] instance.
     * @param controller The [KeyboardController] instance.
     * @param event      The [KeyEvent] instance.
     * @param type       One of the [KeyEventType] constants.
     */
    init {
        button = event.button
        clickCount = event.clickCount
        locationOnScreen = event.locationOnScreen
        point = event.point
        x = event.x
        xOnScreen = event.xOnScreen
        y = event.y
        yOnScreen = event.yOnScreen
        this.controller = controller
        parentEvent = event
    }
}