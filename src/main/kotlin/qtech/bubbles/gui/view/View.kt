package qtech.bubbles.gui.view

import qtech.hydro.GraphicsProcessor
import qtech.bubbles.gui.Widget

abstract class View : Widget() {
    var containerGraphics: GraphicsProcessor? = null
}