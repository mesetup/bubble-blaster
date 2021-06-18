package com.qtech.bubbles.core.controllers

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.event.MouseMotionEvent
import com.qtech.bubbles.event.type.MouseEventType
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

/**
 * @see KeyboardController
 *
 * @see java.awt.event.MouseAdapter
 */
class MouseController private constructor() : MouseAdapter() {
    var currentLocationOnScreen: Point? = null
        private set
    var currentPoint: Point? = null
        private set

    override fun mouseClicked(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(com.qtech.bubbles.event.MouseEvent(BubbleBlaster.instance, this, e, MouseEventType.CLICK))
    }

    override fun mousePressed(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(com.qtech.bubbles.event.MouseEvent(BubbleBlaster.instance, this, e, MouseEventType.PRESS))
    }

    override fun mouseReleased(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(com.qtech.bubbles.event.MouseEvent(BubbleBlaster.instance, this, e, MouseEventType.RELEASE))
    }

    override fun mouseEntered(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(MouseMotionEvent(BubbleBlaster.instance, this, e, MouseEventType.ENTER))
    }

    override fun mouseExited(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(MouseMotionEvent(BubbleBlaster.instance, this, e, MouseEventType.LEAVE))
    }

    override fun mouseDragged(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(MouseMotionEvent(BubbleBlaster.instance, this, e, MouseEventType.DRAG))
    }

    override fun mouseMoved(e: MouseEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(MouseMotionEvent(BubbleBlaster.instance, this, e, MouseEventType.MOTION))
    }

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        currentLocationOnScreen = if (e.locationOnScreen != null) e.locationOnScreen else currentLocationOnScreen
        currentPoint = e.point
        BubbleBlaster.eventBus.post(com.qtech.bubbles.event.MouseWheelEvent(BubbleBlaster.instance, this, e, MouseEventType.MOTION))
    }

    companion object {
        private val INSTANCE = MouseController()
        @JvmStatic
        fun instance(): MouseController {
            return INSTANCE
        }
    }
}