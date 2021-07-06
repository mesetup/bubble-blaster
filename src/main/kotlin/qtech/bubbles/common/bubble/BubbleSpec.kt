package qtech.bubbles.common.bubble

import qtech.bubbles.core.utils.categories.ColorUtils
import java.awt.Color

class BubbleSpec {
    private val circles: List<BubbleCircle>

    private constructor(circles: List<BubbleCircle>) {
        this.circles = circles
    }

    constructor(spec: BubbleSpec) {
        circles = ArrayList(spec.circles)
    }

    class Builder {
        private val circles: MutableList<BubbleCircle>
        private var index = 0
        fun add(color: Color) {
            circles.add(BubbleCircle(index, color))
            index++
        }

        fun add(hex: String) {
            circles.add(BubbleCircle(index, ColorUtils.hex2Rgb(hex)))
            index++
        }

        fun build(): BubbleSpec {
            return BubbleSpec(circles)
        }

        init {
            circles = ArrayList()
        }
    }
}