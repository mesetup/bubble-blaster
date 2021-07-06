package qtech.hydro

import java.awt.Canvas
import java.awt.Image
import java.awt.image.ImageObserver

class ProtectedImageObserver(val canvas: Canvas) : ImageObserver {
    override fun imageUpdate(img: Image?, infoflags: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return canvas.imageUpdate(img, infoflags, x, y, width, height)
    }
}