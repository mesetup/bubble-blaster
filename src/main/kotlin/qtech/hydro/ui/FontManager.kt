package qtech.hydro.ui

import qtech.bubbles.BubbleBlaster
import java.awt.Font
import java.awt.FontFormatException
import java.awt.GraphicsEnvironment
import java.util.*

class FontManager {
    private val ge: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()

    fun create(`class`: Class<*>, path: String): Font {
        val path1: String
        if (!path.startsWith("/")) {
            path1 = "/" + path
        }

        val font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(`class`.getResourceAsStream(path)))
        ge.registerFont(font)
        return font
    }
}
