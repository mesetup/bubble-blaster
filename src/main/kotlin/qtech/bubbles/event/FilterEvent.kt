package qtech.bubbles.event

import java.awt.image.BufferedImageOp

class FilterEvent : Event() {
    val filters: MutableList<BufferedImageOp> = ArrayList()
    fun addFilter(filter: BufferedImageOp) {
        filters.add(filter)
    }
}