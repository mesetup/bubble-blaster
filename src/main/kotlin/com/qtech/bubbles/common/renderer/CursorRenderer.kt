package com.qtech.bubbles.common.renderer

import com.qtech.bubbles.common.ResourceLocation
import com.qtech.bubbles.common.cursor.RegistrableCursor
import com.qtech.bubbles.common.interfaces.Drawable
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.registry.Registry
import java.awt.Cursor
import java.awt.Point
import java.awt.Toolkit
import java.awt.image.BufferedImage

abstract class CursorRenderer(val name: String) : Drawable {
    fun create(): Cursor {

        // Transparent 16 x 16 pixel cursor image.
        val cursorImg2 = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
        val gg2 = GraphicsProcessor(cursorImg2.createGraphics())
        draw(gg2)

        // Create a new blank cursor.
        val cursor = Toolkit.getDefaultToolkit().createCustomCursor(
            cursorImg2, Point(11, 11), name
        )
        Registry.getRegistry(RegistrableCursor::class.java).register(ResourceLocation.fromString("bubbleblaster:$name"), RegistrableCursor(cursor))
        return cursor
    }

    abstract override fun draw(g: GraphicsProcessor)
}