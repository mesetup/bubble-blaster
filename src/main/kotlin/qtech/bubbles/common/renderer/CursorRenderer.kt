package qtech.bubbles.common.renderer

import qtech.bubbles.common.ResourceLocation
import qtech.bubbles.common.cursor.RegistrableCursor
import qtech.bubbles.common.interfaces.Drawable
import qtech.bubbles.registry.Registry
import qtech.hydro.GraphicsProcessor
import qtech.hydro.ui.CursorManager
import java.awt.Cursor
import java.awt.Point
import java.awt.image.BufferedImage

abstract class CursorRenderer(val name: String) : Drawable {
    abstract val hotSpot: Point

    data class CursorCreateContext internal constructor(val name: String)

    fun create(manager: CursorManager): Cursor {
        createContext = CursorCreateContext(name)

        // Transparent 16 x 16 pixel cursor image.
        val image = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
        val processor = GraphicsProcessor(image.createGraphics())
        draw(processor)

        // Create a new blank cursor.
        val cursor = manager.create(name, hotSpot, image)
//        Registry.getRegistry(RegistrableCursor::class.java).register(ResourceLocation.fromString("bubbleblaster:$name"), RegistrableCursor(cursor))
        createContext = null
        return cursor
    }

    abstract override fun draw(g: GraphicsProcessor)

    companion object {
        var createContext: CursorCreateContext? = null
    }
}