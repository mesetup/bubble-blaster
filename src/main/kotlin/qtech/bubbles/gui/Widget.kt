package qtech.bubbles.gui

import qtech.hydro.GraphicsProcessor

abstract class Widget {
    abstract fun paint(gp: GraphicsProcessor)
    abstract fun destroy()
}