package com.qtech.bubbles.graphics

import com.jhlabs.composite.RGBComposite
import java.awt.Color
import java.awt.CompositeContext
import java.awt.RenderingHints
import java.awt.image.ColorModel

class HueComposite : RGBComposite {
    private val hue: Float

    constructor(alpha: Float, hue: Int) : super(alpha) {
        this.hue = (hue % 360).toFloat() / 360
    }

    constructor(alpha: Float, hue: Float) : super(alpha) {
        this.hue = hue % 360f / 360
    }

    override fun createContext(srcColorModel: ColorModel, dstColorModel: ColorModel, hints: RenderingHints): CompositeContext {
        return Context(hue, extraAlpha, srcColorModel, dstColorModel)
    }

    internal class Context(private val hue: Float, alpha: Float, srcColorModel: ColorModel?, dstColorModel: ColorModel?) : RGBCompositeContext(alpha, srcColorModel, dstColorModel) {
        private val sHSB = FloatArray(3)
        private val dHSB = FloatArray(3)
        override fun composeRGB(src: IntArray, dst: IntArray, alpha: Float) {
            val w = src.size
            var i = 0
            while (i < w) {
                val sr = src[i]
                val dir = dst[i]
                val sg = src[i + 1]
                val dig = dst[i + 1]
                val sb = src[i + 2]
                val dib = dst[i + 2]
                val sa = src[i + 3]
                val dia = dst[i + 3]
                var dor: Int
                var dog: Int
                var dob: Int
                Color.RGBtoHSB(sr, sg, sb, sHSB)
                Color.RGBtoHSB(dir, dig, dib, dHSB)
                dHSB[0] = sHSB[0] + hue
                val doRGB = Color.HSBtoRGB(dHSB[0], dHSB[1], dHSB[2])
                dor = doRGB and 0xff0000 shr 16
                dog = doRGB and 0xff00 shr 8
                dob = doRGB and 0xff
                val a = alpha * sa / 255f
                val ac = 1 - a
                dst[i] = (a * dor + ac * dir).toInt()
                dst[i + 1] = (a * dog + ac * dig).toInt()
                dst[i + 2] = (a * dob + ac * dib).toInt()
                dst[i + 3] = (sa * alpha + dia * ac).toInt()
                i += 4
            }
        }
    }
}