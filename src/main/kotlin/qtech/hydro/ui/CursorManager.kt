package qtech.hydro.ui

import qtech.hydro.manager.IManager
import java.awt.Cursor
import java.awt.Image
import java.awt.Point
import java.awt.Toolkit
import java.awt.image.BufferedImage

class CursorManager internal constructor() : IManager {
    internal var currentContext: CursorLoadContext? = null

    internal data class CursorLoadContext(val name: String, val image: Image)

    private val cursors: MutableMap<String, Cursor> = HashMap()
    private val toolkit = Toolkit.getDefaultToolkit()

    fun create(name: String, image: Image): Cursor {
        return this.create(name, Point(0, 0), image)
    }

    fun create(name: String, hotSpot: Point, image: Image): Cursor {
        this.currentContext = CursorLoadContext(name, image)
        val cursor = toolkit.createCustomCursor(image, hotSpot, name)
        this.cursors[name] = cursor
        this.currentContext = null
        return cursor
    }

    fun create(name: String, hotSpotX: Int, hotSpotY: Int, image: Image): Cursor {
        return this.create(name, Point(hotSpotX, hotSpotY), image)
    }
}
