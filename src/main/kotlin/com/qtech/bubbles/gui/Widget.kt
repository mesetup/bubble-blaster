package com.qtech.bubbles.gui

import com.qtech.bubbles.graphics.GraphicsProcessor

abstract class Widget {
    abstract fun paint(gp: GraphicsProcessor)
    abstract fun destroy()
}