package com.qtech.bubbles.graphics

import com.jhlabs.image.*
import org.jdesktop.swingx.image.AbstractFilter
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToLong

class FilterApplier(size: Dimension, observer: ImageObserver) {
    val graphics: GraphicsProcessor
    private val buffer: BufferedImage
    private val observer: ImageObserver
    private var filters: ArrayList<Any>? = null
    val bounds: Rectangle
        get() {
            for (filter in filters!!) {
                if (filter is GaussianFilter) {
                    val radius = filter.radius
                    return Rectangle(
                        (-ceil(radius.toDouble()).roundToLong()).toInt(), (-ceil(radius.toDouble()).roundToLong()).toInt(),
                        ceil((buffer.width + radius * 2).toDouble()).roundToLong().toInt(), ceil((buffer.height + radius * 2).toDouble()).roundToLong().toInt()
                    )
                }
            }
            return Rectangle(0, 0, buffer.width, buffer.height)
        }

    fun getBounds(x: Int, y: Int, w: Int, h: Int): Rectangle {
        return Rectangle(x, y, w, h)
    }

    fun getBounds(rect: Rectangle): Rectangle {
        return getBounds(rect.x, rect.y, rect.width, rect.height)
    }

    fun setFilters(filters: ArrayList<Any>?) {
        this.filters = filters
    }

    fun done(): BufferedImage {
        val bounds = bounds
        val outputBuffer = BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB)
        outputBuffer.createGraphics().drawImage(buffer, 0, 0, observer)
        for (filter in filters!!) {
            when (filter) {
                is ConvolveFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is PointFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is TransformFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is BinaryFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is WholeImageFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is ShadowFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is ShatterFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is BoxBlurFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                is AbstractFilter -> {
                    filter.filter(outputBuffer, outputBuffer)
                }
                else -> {
                    throw NullPointerException()
                }
            }
        }
        return outputBuffer
    }

    init {
        size.width = max(size.width, 1)
        size.height = max(size.height, 1)
        val buffer = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)
        val g2d = GraphicsProcessor(buffer.createGraphics())
        this.observer = observer
        graphics = g2d
        this.buffer = buffer
    }
}