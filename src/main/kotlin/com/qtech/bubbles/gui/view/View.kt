package com.qtech.bubbles.gui.view

import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.gui.Widget

abstract class View : Widget() {
    var containerGraphics: GraphicsProcessor? = null
}