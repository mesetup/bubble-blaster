package com.qtech.bubbles.core.utils.categories

import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.Font
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

object GraphicsUtils {
    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g    The GraphicsProcessor instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text within.
     * @param font The Font for the text.
     * @author **Danier Kvist** [from this answer](https://stackoverflow.com/a/27740330/11124294).
     */
    @JvmStatic
    fun drawCenteredString(g: GraphicsProcessor, text: String, rect: Rectangle2D, font: Font?) {
        // Get the FontMetrics
        val metrics = g.fontMetrics(font)

        // Determine the X coordinate for the text
        val x = (rect.x + (rect.width - metrics.stringWidth(text)) / 2).toInt()

        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        val y = (rect.y + (rect.height - metrics.height) / 2 + metrics.ascent).toInt()

        // Set the font
        g.font(font)

        // Draw the String
        g.textI(text, x, y)
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g      The GraphicsProcessor instance.
     * @param text   The String to draw.
     * @param point  The Point to center the text within.
     * @param height The height to center the text within.
     * @param font   The Font for the text.
     * @author **Danier Kvist** [from this answer](https://stackoverflow.com/a/27740330/11124294).
     */
    @JvmStatic
    fun drawRightAnchoredString(g: GraphicsProcessor, text: String, point: Point2D, height: Double, font: Font?) {
        // Get the FontMetrics
        val metrics = g.fontMetrics(font)

        // Determine the X coordinate for the text
        val x = (point.x - metrics.stringWidth(text)).toInt()

        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        val y = (point.y + (height - metrics.height) / 2 + metrics.ascent).toInt()

        // Set the font
        g.font(font)

        // Draw the String
        g.textI(text, x, y)
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g      The GraphicsProcessor instance.
     * @param text   The String to draw.
     * @param point  The Point to center the text within.
     * @param height The height to center the text within.
     * @param font   The Font for the text.
     * @author **Danier Kvist** [from this answer](https://stackoverflow.com/a/27740330/11124294).
     */
    @JvmStatic
    fun drawLeftAnchoredString(g: GraphicsProcessor, text: String?, point: Point2D, height: Double, font: Font?) {
        // Get the FontMetrics
        val metrics = g.fontMetrics(font)

        // Determine the X coordinate for the text
        val x = point.x.toInt()

        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        val y = (point.y + (height - metrics.height) / 2 + metrics.ascent).toInt()

        // Set the font
        g.font(font)

        // Draw the String
        g.textI(text, x, y)
    }
}