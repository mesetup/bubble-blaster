@file:Suppress("unused")

package com.qtech.bubbles.graphics

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawCenteredString
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawLeftAnchoredString
import com.qtech.bubbles.core.utils.categories.GraphicsUtils.drawRightAnchoredString
import com.qtech.bubbles.core.utils.categories.StringUtils.createFallbackString
import com.qtech.bubbles.core.utils.categories.StringUtils.splitIntoLines
import com.qtech.bubbles.core.utils.categories.StringUtils.wrap
import com.qtech.bubbles.graphics.shapes.Circle
import com.qtech.bubbles.graphics.shapes.CircleDouble
import com.qtech.bubbles.graphics.shapes.CircleFloat
import org.apache.commons.lang3.StringUtils
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.GlyphVector
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.image.ImageObserver
import java.awt.image.RenderedImage
import java.awt.image.renderable.RenderableImage
import java.text.AttributedCharacterIterator

class GraphicsProcessor {
    private val gg: Graphics2D
    private var fallbackFont: Font? = null

    constructor(g2d: Graphics2D) {
        gg = g2d
    }

    constructor(g: Graphics) {
        gg = g as Graphics2D
    }

    constructor(gg: GraphicsProcessor) {
        this.gg = gg.wrapped()
        fallbackFont = gg.fallbackFont
    }

    fun img(img: Image?, xform: AffineTransform?, obs: ImageObserver?): Boolean {
        return gg.drawImage(img, xform, obs)
    }

    fun img(img: BufferedImage?, op: BufferedImageOp?, x: Int, y: Int): GraphicsProcessor {
        gg.drawImage(img, op, x, y)
        return this
    }

    fun renderedImg(img: RenderedImage?, xform: AffineTransform?): GraphicsProcessor {
        gg.drawRenderedImage(img, xform)
        return this
    }

    fun renderableImg(img: RenderableImage?, xform: AffineTransform?): GraphicsProcessor {
        gg.drawRenderableImage(img, xform)
        return this
    }

    fun textI(str: String?, x: Int, y: Int): GraphicsProcessor {
        gg.drawString(createFallbackString(str!!, font()).iterator, x, y)
        return this
    }

    fun textF(str: String?, x: Float, y: Float): GraphicsProcessor {
        gg.drawString(createFallbackString(str!!, font()).iterator, x, y)
        return this
    }

    fun textI(iterator: AttributedCharacterIterator?, x: Int, y: Int): GraphicsProcessor {
        gg.drawString(iterator, x, y)
        return this
    }

    fun textF(iterator: AttributedCharacterIterator?, x: Float, y: Float): GraphicsProcessor {
        gg.drawString(iterator, x, y)
        return this
    }

    fun img(img: Image?, x: Int, y: Int): GraphicsProcessor {
        gg.drawImage(img, x, y, BubbleBlaster.instance)
        return this
    }

    fun img(img: Image?, x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.drawImage(img, x, y, width, height, BubbleBlaster.instance)
        return this
    }

    fun img(img: Image?, x: Int, y: Int, backgroundColor: Color?): GraphicsProcessor {
        gg.drawImage(img, x, y, backgroundColor, BubbleBlaster.instance)
        return this
    }

    fun img(img: Image?, x: Int, y: Int, width: Int, height: Int, backgroundColor: Color?): GraphicsProcessor {
        gg.drawImage(img, x, y, width, height, backgroundColor, BubbleBlaster.instance)
        return this
    }

    fun img(img: Image?, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int): GraphicsProcessor {
        gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, BubbleBlaster.instance)
        return this
    }

    fun img(img: Image?, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int, backgroundColor: Color?): GraphicsProcessor {
        gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, backgroundColor, BubbleBlaster.instance)
        return this
    }

    fun dispose(): GraphicsProcessor {
        gg.dispose()
        return this
    }

    fun glyph(g: GlyphVector?, x: Float, y: Float): GraphicsProcessor {
        gg.drawGlyphVector(g, x, y)
        return this
    }

    fun shape(s: Shape?): GraphicsProcessor {
        gg.draw(s)
        return this
    }

    fun shapeF(s: Shape?): GraphicsProcessor {
        gg.fill(s)
        return this
    }

    fun hit(rect: Rectangle?, s: Shape?, onStroke: Boolean): Boolean {
        return gg.hit(rect, s, onStroke)
    }

    val deviceConfiguration: GraphicsConfiguration
        get() = gg.deviceConfiguration

    fun composite(): Composite {
        return gg.composite
    }

    fun composite(comp: Composite?): GraphicsProcessor {
        gg.composite = comp
        return this
    }

    fun paint(): Paint {
        return gg.paint
    }

    fun paint(paint: Paint?): GraphicsProcessor {
        gg.paint = paint
        return this
    }

    fun stroke(): Stroke {
        return gg.stroke
    }

    fun stroke(s: Stroke?): GraphicsProcessor {
        gg.stroke = s
        return this
    }

    fun hint(hintKey: RenderingHints.Key?, hintValue: Any?): GraphicsProcessor {
        gg.setRenderingHint(hintKey, hintValue)
        return this
    }

    fun hint(hintKey: RenderingHints.Key?): Any {
        return gg.getRenderingHint(hintKey)
    }

    fun hints(hints: Map<*, *>?): GraphicsProcessor {
        gg.setRenderingHints(hints)
        return this
    }

    fun addHint(hints: Map<*, *>?): GraphicsProcessor {
        gg.addRenderingHints(hints)
        return this
    }

    fun hints(): RenderingHints {
        return gg.renderingHints
    }

    fun create(): GraphicsProcessor {
        return GraphicsProcessor(gg.create())
    }

    fun translate(x: Int, y: Int): GraphicsProcessor {
        gg.translate(x, y)
        return this
    }

    fun color(): Color {
        return gg.color
    }

    fun color(c: Color?): GraphicsProcessor {
        gg.color = c
        return this
    }

    fun paintMode(): GraphicsProcessor {
        gg.setPaintMode()
        return this
    }

    fun xorMode(c1: Color?): GraphicsProcessor {
        gg.setXORMode(c1)
        return this
    }

    fun background(): Color {
        return gg.background
    }

    fun background(color: Color?): GraphicsProcessor {
        gg.background = color
        return this
    }

    fun font(): Font {
        return gg.font
    }

    fun font(font: Font?): GraphicsProcessor {
        gg.font = font
        return this
    }

    fun fontMetrics(f: Font?): FontMetrics {
        return gg.getFontMetrics(f)
    }

    fun fontMetrics(): FontMetrics {
        return gg.fontMetrics
    }

    fun clipBounds(): Rectangle {
        return gg.clipBounds
    }

    fun clipRect(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.clipRect(x, y, width, height)
        return this
    }

    fun clip1(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.setClip(x, y, width, height)
        return this
    }

    fun clip(): Shape {
        return gg.clip
    }

    fun clip1(clip: Shape?): GraphicsProcessor {
        gg.clip = clip
        return this
    }

    fun copyArea(x: Int, y: Int, width: Int, height: Int, dx: Int, dy: Int): GraphicsProcessor {
        gg.copyArea(x, y, width, height, dx, dy)
        return this
    }

    fun line(x1: Int, y1: Int, x2: Int, y2: Int): GraphicsProcessor {
        gg.drawLine(x1, y1, x2, y2)
        return this
    }

    fun rect(x1: Int, y1: Int, x2: Int, y2: Int): GraphicsProcessor {
        gg.drawRect(x1, y1, x2, y2)
        return this
    }

    fun rectF(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.fillRect(x, y, width, height)
        return this
    }

    fun clear(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.clearRect(x, y, width, height)
        return this
    }

    fun roundRect(x: Int, y: Int, width: Int, height: Int, arcWidth: Int, arcHeight: Int): GraphicsProcessor {
        gg.drawRoundRect(x, y, width, height, arcWidth, arcHeight)
        return this
    }

    fun roundRectF(x: Int, y: Int, width: Int, height: Int, arcWidth: Int, arcHeight: Int): GraphicsProcessor {
        gg.fillRoundRect(x, y, width, height, arcWidth, arcHeight)
        return this
    }

    fun oval(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.drawOval(x, y, width, height)
        return this
    }

    fun ovalF(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        gg.fillOval(x, y, width, height)
        return this
    }

    fun oval(x: Float, y: Float, width: Float, height: Float): GraphicsProcessor {
        gg.draw(Ellipse2D.Float(x, y, width, height))
        return this
    }

    fun ovalF(x: Float, y: Float, width: Float, height: Float): GraphicsProcessor {
        gg.fill(Ellipse2D.Float(x, y, width, height))
        return this
    }

    fun oval(x: Double, y: Double, width: Double, height: Double): GraphicsProcessor {
        gg.draw(Ellipse2D.Double(x, y, width, height))
        return this
    }

    fun ovalF(x: Double, y: Double, width: Double, height: Double): GraphicsProcessor {
        gg.fill(Ellipse2D.Double(x, y, width, height))
        return this
    }

    fun circ(x: Int, y: Int, radius: Int): GraphicsProcessor {
        gg.draw(Ellipse2D.Float(x.toFloat(), y.toFloat(), radius.toFloat(), radius.toFloat()))
        return this
    }

    fun circF(x: Int, y: Int, radius: Int): GraphicsProcessor {
        gg.fill(Ellipse2D.Float(x.toFloat(), y.toFloat(), radius.toFloat(), radius.toFloat()))
        return this
    }

    fun circ(x: Float, y: Float, radius: Float): GraphicsProcessor {
        gg.draw(Ellipse2D.Float(x, y, radius, radius))
        return this
    }

    fun circF(x: Float, y: Float, radius: Float): GraphicsProcessor {
        gg.fill(Ellipse2D.Float(x, y, radius, radius))
        return this
    }

    fun circ(x: Double, y: Double, radius: Double): GraphicsProcessor {
        gg.draw(Ellipse2D.Double(x, y, radius, radius))
        return this
    }

    fun circF(x: Double, y: Double, radius: Double): GraphicsProcessor {
        gg.fill(Ellipse2D.Double(x, y, radius, radius))
        return this
    }

    fun circ(circle: Circle?): GraphicsProcessor {
        gg.draw(circle)
        return this
    }

    fun circF(circle: Circle?): GraphicsProcessor {
        gg.fill(circle)
        return this
    }

    fun circ(circle: CircleFloat?): GraphicsProcessor {
        gg.draw(circle)
        return this
    }

    fun circF(circle: CircleFloat?): GraphicsProcessor {
        gg.fill(circle)
        return this
    }

    fun circ(circle: CircleDouble?): GraphicsProcessor {
        gg.draw(circle)
        return this
    }

    fun circF(circle: CircleDouble?): GraphicsProcessor {
        gg.fill(circle)
        return this
    }

    fun arc(x: Int, y: Int, width: Int, height: Int, startAngle: Int, arcAngle: Int): GraphicsProcessor {
        gg.drawArc(x, y, width, height, startAngle, arcAngle)
        return this
    }

    fun arcF(x: Int, y: Int, width: Int, height: Int, startAngle: Int, arcAngle: Int): GraphicsProcessor {
        gg.fillArc(x, y, width, height, startAngle, arcAngle)
        return this
    }

    fun polyLine(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int): GraphicsProcessor {
        gg.drawPolyline(xPoints, yPoints, nPoints)
        return this
    }

    fun poly(polygon: Polygon?): GraphicsProcessor {
        gg.drawPolygon(polygon)
        return this
    }

    fun poly(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int): GraphicsProcessor {
        gg.drawPolygon(xPoints, yPoints, nPoints)
        return this
    }

    fun polyF(polygon: Polygon?): GraphicsProcessor {
        gg.fillPolygon(polygon)
        return this
    }

    fun polyF(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int): GraphicsProcessor {
        gg.fillPolygon(xPoints, yPoints, nPoints)
        return this
    }

    fun translate(tx: Double, ty: Double): GraphicsProcessor {
        gg.translate(tx, ty)
        return this
    }

    fun rotate(theta: Double): GraphicsProcessor {
        gg.rotate(theta)
        return this
    }

    fun rotate(theta: Double, x: Double, y: Double): GraphicsProcessor {
        gg.rotate(theta, x, y)
        return this
    }

    fun scale(sx: Double, sy: Double): GraphicsProcessor {
        gg.scale(sx, sy)
        return this
    }

    fun shear(shx: Double, shy: Double): GraphicsProcessor {
        gg.shear(shx, shy)
        return this
    }

    fun transform(Tx: AffineTransform?): GraphicsProcessor {
        gg.transform(Tx)
        return this
    }

    fun transform1(Tx: AffineTransform?): GraphicsProcessor {
        gg.transform = Tx
        return this
    }

    fun transform(): AffineTransform {
        return gg.transform
    }

    fun clip(s: Shape?): GraphicsProcessor {
        gg.clip(s)
        return this
    }

    val fontRenderContext: FontRenderContext
        get() = gg.fontRenderContext

    fun fallbackFont(font: Font?): GraphicsProcessor {
        fallbackFont = font
        return this
    }

    fun fallbackFont(): Font? {
        return fallbackFont
    }

    @Deprecated("")
    fun multilineText(str: String, x: Int, y: Int): GraphicsProcessor {
        var y1 = y
        y1 -= gg.fontMetrics.height
        for (line in str.split("\n").toTypedArray()) textI(line, x, gg.fontMetrics.height.let { y1 += it; y1 })
        return this
    }

    @Suppress("DEPRECATION")
    @Deprecated("")
    fun wrappedText(str: String?, x: Int, y: Int, maxWidth: Int): GraphicsProcessor {
        val lines = wrap(str!!, fontMetrics(font()), maxWidth)
        val joined: String = StringUtils.join(lines.toTypedArray(), '\n')
        multilineText(joined, x, y)
        return this
    }

    @Deprecated("")
    fun tabText(str: String, x: Int, y: Int): GraphicsProcessor {
        var x1 = x
        for (line in str.split("\t").toTypedArray()) textI(line, gg.fontMetrics.stringWidth("    ").let { x1 += it; x1 }, y)
        return this
    }

    @Deprecated("")
    fun leftText(text: String?, pnt: Point?, height: Int): GraphicsProcessor {
        drawLeftAnchoredString(this, text, pnt!!, height.toDouble(), font())
        return this
    }

    @Deprecated("")
    fun centerText(text: String?, rect: Rectangle?): GraphicsProcessor {
        drawCenteredString(this, text!!, rect!!, font())
        return this
    }

    @Deprecated("")
    fun rightText(text: String?, pnt: Point?, height: Int): GraphicsProcessor {
        drawRightAnchoredString(this, text!!, pnt!!, height.toDouble(), font())
        return this
    }

    fun centerText(text: String, x: Int, y: Int, multiline: Boolean, maxWidth: Int, wrap: Boolean, tab: Boolean, anchor: Anchor?): GraphicsProcessor {
        var x1 = x
        var y1 = y
        val metrics = fontMetrics(font())
        return when {
            wrap -> {
                val lines = wrap(text, fontMetrics(font()), maxWidth)
                val joined: String = StringUtils.join(lines.toTypedArray(), '\n')
                if (multiline) {
                    y1 -= gg.fontMetrics.height
                    for (line in splitIntoLines(joined)) centerText(line, x1, gg.fontMetrics.height.let { y1 += it; y1 }, false, maxWidth, false, tab, anchor)
                } else {
                    y1 -= gg.fontMetrics.height
                    for (line in lines) centerText(line, x1, gg.fontMetrics.height.let { y1 += it; y1 }, false, maxWidth, false, tab, anchor)
                }
                this
            }
            multiline -> {
                y1 -= gg.fontMetrics.height
                for (line in splitIntoLines(text)) centerText(line, x1, gg.fontMetrics.height.let { y1 += it; y1 }, false, maxWidth, false, tab, anchor)
                this
            }
            tab -> {
                for (line in text.split("\t").toTypedArray()) centerText(line, gg.fontMetrics.charWidth(' ') * (gg.fontMetrics.maxAdvance * 4).let { x1 += it; x1 }, y1, false, maxWidth, wrap = false, tab = false, anchor = anchor)
                this
            }
            else -> {
                val x2 = x1 + metrics.stringWidth(text) / 2
                val y2 = y1 + metrics.height / 2 + metrics.ascent
                textI(text, x2, y2)
                this
            }
        }
    }

    fun create(x: Int, y: Int, width: Int, height: Int): GraphicsProcessor {
        return GraphicsProcessor(gg.create(x, y, width, height))
    }

    fun create(rect: Rectangle): GraphicsProcessor {
        return GraphicsProcessor(gg.create(rect.x, rect.y, rect.width, rect.height))
    }

    fun wrapped(): Graphics2D {
        return gg
    }
}