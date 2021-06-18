package com.qtech.bubbles.util.helpers

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.graphics.GraphicsProcessor
import com.qtech.bubbles.settings.GameSettings.Companion.instance
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.bridge.BridgeContext
import org.apache.batik.bridge.GVTBuilder
import org.apache.batik.bridge.UserAgentAdapter
import org.apache.batik.ext.awt.RenderingHintsKeyExt
import org.apache.batik.gvt.GraphicsNode
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.svg.SVGDocument
import java.awt.Color
import java.awt.Image
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.InputStream
import java.net.URL

/**
 * Immutable class to get the Image representation of a svg resource.
 */
class SvgHelper {
    /**
     * Get the svg root node of the document.
     *
     * @return svg root node.
     */
    /**
     * Root node of svg document
     */
    val rootSvgNode: GraphicsNode
    /**
     * Get the svg document.
     *
     * @return the svg document.
     */
    /**
     * Loaded SVG document
     */
    val svgDocument: SVGDocument

    /**
     * Load the svg resource from a URL & InputStream into a document.
     *
     * @param url location of svg resource.
     * @throws java.io.IOException when svg resource cannot be read.
     */
    constructor(url: URL, stream: InputStream?) {
        val parser = XMLResourceDescriptor.getXMLParserClassName()
        val factory = SAXSVGDocumentFactory(parser)
        svgDocument = factory.createDocument(url.toString(), stream) as SVGDocument
        rootSvgNode = getRootNode(svgDocument)
    }

    /**
     * Load the svg resource from a URL into a document.
     *
     * @param url location of svg resource.
     * @throws java.io.IOException when svg resource cannot be read.
     */
    constructor(url: URL) {
        val parser = XMLResourceDescriptor.getXMLParserClassName()
        val factory = SAXSVGDocumentFactory(parser)
        svgDocument = factory.createDocument(url.toString()) as SVGDocument
        rootSvgNode = getRootNode(svgDocument)
    }

    /**
     * Load the svg from a document.
     *
     * @param document svg resource
     */
    constructor(document: SVGDocument) {
        svgDocument = document
        rootSvgNode = getRootNode(svgDocument)
    }

    /**
     * Renders and returns the svg based image.
     *
     * @param width  desired width, if it is less than or equal to 0 aspect
     * ratio is preserved and the size is determined by height.
     * @param height desired height, if it is less than or equal to 0 aspect
     * ratio is preserved and the size is determined by width.
     * @return image of the rendered svg.'
     * TODO: modify to also give a image that preserves aspects but matches
     * width or height individually.
     */
    fun getImage(width: Int, height: Int): Image {
        /* Adjusts the scale of the transformation below, if either width or
         * height is less than or equal to 0 the aspect ratio is preserved.
         */
        var width1 = width
        var height1 = height
        val bounds = rootSvgNode.primitiveBounds
        val scaleX: Double
        val scaleY: Double
        if (width1 <= 0) {
            scaleY = height1 / bounds.height
            scaleX = scaleY
            width1 = (scaleX * bounds.width).toInt()
        } else if (height1 <= 0) {
            scaleY = width1 / bounds.width
            scaleX = scaleY
            height1 = (scaleY * bounds.height).toInt()
        } else {
            scaleX = width1 / bounds.width
            scaleY = height1 / bounds.height
        }

        // Paint svg into image buffer
        val bufferedImage = BufferedImage(width1,
            height1, BufferedImage.TYPE_INT_ARGB)
        val g2d = GraphicsProcessor(bufferedImage.graphics)

        // For a smooth graphic with no jagged edges or rasterized look.
        if (BubbleBlaster.instance.graphicsEngine.isAntialiasingEnabled && instance().isAntialiasEnabled) g2d.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.hint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2d.hint(RenderingHintsKeyExt.KEY_TRANSCODING, RenderingHintsKeyExt.VALUE_TRANSCODING_PRINTING)

        // Scale image to desired size
        val usr2dev = AffineTransform(scaleX, 0.0, 0.0, scaleY, 0.0, 0.0)
        g2d.transform(usr2dev)
        rootSvgNode.paint(g2d.wrapped())

        // Cleanup and return image
        g2d.dispose()
        return bufferedImage
    }

    /**
     * Renders and returns the svg based image.
     *
     * @param width  desired width, if it is less than or equal to 0 aspect ratio is preserved and the size is determined by height.
     * @param height desired height, if it is less than or equal to 0 aspect ratio is preserved and the size is determined by width.
     * @param fill   desired fill color.
     * @return image of the rendered svg.'
     * TODO: modify to also give a image that preserves aspects but matches
     * width or height individually.
     */
    fun getColoredImage(width: Int, height: Int, fill: Color?): Image {
        return getColoredImage(width, height, fill, null)
    }

    /**
     * Renders and returns the svg based image.
     *
     * @param width   desired width, if it is less than or equal to 0 aspect ratio is preserved and the size is determined by height.
     * @param height  desired height, if it is less than or equal to 0 aspect ratio is preserved and the size is determined by width.
     * @param fill    desired fill color.
     * @param outline desired outline color.
     * @return image of the rendered svg.'
     * TODO: modify to also give a image that preserves aspects but matches
     * width or height individually.
     */
    fun getColoredImage(width: Int, height: Int, fill: Color?, outline: Color?): Image {
        /* Adjusts the scale of the transformation below, if either width or
         * height is less than or equal to 0 the aspect ratio is preserved.
         */
        var width1 = width
        var height1 = height
        val bounds = rootSvgNode.primitiveBounds
        val scaleX: Double
        val scaleY: Double
        if (width1 <= 0) {
            scaleY = height1 / bounds.height
            scaleX = scaleY
            width1 = (scaleX * bounds.width).toInt()
        } else if (height1 <= 0) {
            scaleY = width1 / bounds.width
            scaleX = scaleY
            height1 = (scaleY * bounds.height).toInt()
        } else {
            scaleX = width1 / bounds.width
            scaleY = height1 / bounds.height
        }

        // Paint svg into image buffer
        val bufferedImage = BufferedImage(width1,
            height1, BufferedImage.TYPE_INT_ARGB)
        val g2d = GraphicsProcessor(bufferedImage.graphics)

        // For a smooth graphic with no jagged edges or rasterized look.
        if (BubbleBlaster.instance.graphicsEngine.isAntialiasingEnabled && instance().isAntialiasEnabled) g2d.hint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.hint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2d.hint(RenderingHintsKeyExt.KEY_TRANSCODING, RenderingHintsKeyExt.VALUE_TRANSCODING_PRINTING)

        // Scale image to desired size
        val usr2dev = AffineTransform(scaleX, 0.0, 0.0, scaleY, 0.0, 0.0)
        g2d.transform(usr2dev)
        if (fill != null) {
            g2d.color(fill)
            g2d.shapeF(rootSvgNode.outline)
        }
        if (outline != null) {
            g2d.color(outline)
            g2d.shapeF(rootSvgNode.outline)
        }

        // Cleanup and return image
        g2d.dispose()
        return bufferedImage
    }

    companion object {
        /**
         * Get svg root from the given document.
         *
         * @param document svg resource
         */
        private fun getRootNode(document: SVGDocument): GraphicsNode {
            // Build the tree and get the document dimensions
            val userAgentAdapter = UserAgentAdapter()
            val bridgeContext = BridgeContext(userAgentAdapter)
            val builder = GVTBuilder()
            return builder.build(bridgeContext, document)
        }
    }
}