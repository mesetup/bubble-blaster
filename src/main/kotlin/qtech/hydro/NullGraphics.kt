package qtech.hydro

import qtech.bubbles.BubbleBlaster
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.GlyphVector
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.image.ImageObserver
import java.awt.image.RenderedImage
import java.awt.image.renderable.RenderableImage
import java.text.AttributedCharacterIterator

class NullGraphics : Graphics2D() {
    private var composite: Composite? = null
    private var paint: Paint = Color(0, 0, 0)
    private var color = Color(0, 0, 0)
    private var font = BubbleBlaster.instance.font
    private var background: Color? = null
    private val stroke: Stroke? = null
    override fun create(): NullGraphics {
        return NullGraphics()
    }

    override fun translate(i: Int, i1: Int) {}
    override fun translate(v: Double, v1: Double) {}
    override fun rotate(v: Double) {}
    override fun rotate(v: Double, v1: Double, v2: Double) {}
    override fun scale(v: Double, v1: Double) {}
    override fun shear(v: Double, v1: Double) {}
    override fun transform(affineTransform: AffineTransform) {}
    override fun setTransform(affineTransform: AffineTransform) {}
    override fun getTransform(): AffineTransform? {
        return null
    }

    override fun getPaint(): Paint {
        return paint
    }

    override fun getComposite(): Composite {
        return composite!!
    }

    override fun setBackground(color: Color) {
        background = color
    }

    override fun getBackground(): Color {
        return background!!
    }

    override fun getStroke(): Stroke {
        return stroke!!
    }

    override fun clip(shape: Shape) {}
    override fun getFontRenderContext(): FontRenderContext? {
        return null
    }

    override fun getColor(): Color {
        return color
    }

    override fun setColor(color: Color) {
        this.color = color
        paint = color
    }

    override fun setPaintMode() {}
    override fun setXORMode(color: Color) {}
    override fun getFont(): Font {
        return font
    }

    override fun setFont(font: Font) {
        this.font = font
    }

    override fun getFontMetrics(font: Font): FontMetrics? {
        return null
    }

    override fun getClipBounds(): Rectangle {
        return Rectangle(0, 0, 0, 0)
    }

    override fun clipRect(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun setClip(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun getClip(): Shape {
        return NullShape()
    }

    override fun setClip(shape: Shape) {}
    override fun copyArea(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int) {}
    override fun drawLine(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun fillRect(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun clearRect(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun drawRoundRect(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int) {}
    override fun fillRoundRect(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int) {}
    override fun drawOval(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun fillOval(i: Int, i1: Int, i2: Int, i3: Int) {}
    override fun drawArc(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int) {}
    override fun fillArc(i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int) {}
    override fun drawPolyline(ints: IntArray, ints1: IntArray, i: Int) {}
    override fun drawPolygon(ints: IntArray, ints1: IntArray, i: Int) {}
    override fun fillPolygon(ints: IntArray, ints1: IntArray, i: Int) {}
    override fun draw(shape: Shape) {}
    override fun drawImage(image: Image, affineTransform: AffineTransform, imageObserver: ImageObserver): Boolean {
        return true
    }

    override fun drawImage(bufferedImage: BufferedImage, bufferedImageOp: BufferedImageOp, i: Int, i1: Int) {}
    override fun drawRenderedImage(renderedImage: RenderedImage, affineTransform: AffineTransform) {}
    override fun drawRenderableImage(renderableImage: RenderableImage, affineTransform: AffineTransform) {}
    override fun drawString(s: String, i: Int, i1: Int) {}
    override fun drawString(s: String, v: Float, v1: Float) {}
    override fun drawString(attributedCharacterIterator: AttributedCharacterIterator, i: Int, i1: Int) {}
    override fun drawString(attributedCharacterIterator: AttributedCharacterIterator, v: Float, v1: Float) {}
    override fun drawGlyphVector(glyphVector: GlyphVector, v: Float, v1: Float) {}
    override fun fill(shape: Shape) {}
    override fun hit(rectangle: Rectangle, shape: Shape, b: Boolean): Boolean {
        return false
    }

    override fun getDeviceConfiguration(): GraphicsConfiguration {
        return Game.instance.graphicsConfiguration
    }

    override fun setComposite(composite: Composite) {
        this.composite = composite
    }

    override fun setPaint(paint: Paint) {
        this.paint = paint
    }

    override fun setStroke(stroke: Stroke) {}
    override fun setRenderingHint(key: RenderingHints.Key, o: Any) {}
    override fun getRenderingHint(key: RenderingHints.Key): Any? {
        return null
    }

    override fun setRenderingHints(map: Map<*, *>?) {}
    override fun addRenderingHints(map: Map<*, *>?) {}
    override fun getRenderingHints(): RenderingHints {
        return RenderingHints(HashMap<RenderingHints.Key, Any?>())
    }

    override fun drawImage(image: Image, i: Int, i1: Int, imageObserver: ImageObserver): Boolean {
        return false
    }

    override fun drawImage(image: Image, i: Int, i1: Int, i2: Int, i3: Int, imageObserver: ImageObserver): Boolean {
        return false
    }

    override fun drawImage(image: Image, i: Int, i1: Int, color: Color, imageObserver: ImageObserver): Boolean {
        return false
    }

    override fun drawImage(image: Image, i: Int, i1: Int, i2: Int, i3: Int, color: Color, imageObserver: ImageObserver): Boolean {
        return false
    }

    override fun drawImage(image: Image, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, imageObserver: ImageObserver): Boolean {
        return false
    }

    override fun drawImage(image: Image, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int, color: Color, imageObserver: ImageObserver): Boolean {
        return false
    }

    override fun dispose() {}
}