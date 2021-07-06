package qtech.hydro

class OuterBorder : Border {
    constructor(insets: Insets) : super(insets)
    constructor(top: Int, left: Int, bottom: Int, right: Int) : super(top, left, bottom, right)

    /**
     * Paints a border.
     *
     * @param g      the graphics.
     * @param x      the x-positon.
     * @param y      the y-position.
     * @param width  the width.
     * @param height the height.
     */
    override fun paintBorder(g: GraphicsProcessor, x: Int, y: Int, width: Int, height: Int) {
        // Get insets
        val insets = borderInsets

        // Save old paint.
        val oldPaint = g.paint()

        // Set paint.
        g.paint(paint)

        // Draw rectangles around the component, but do not draw
        // in the component area itself.
        g.rectF(x - insets.left, y - insets.top, width + insets.left + insets.right, insets.top)
        g.rectF(x - insets.left, y, insets.left, height)
        g.rectF(x + width, y, insets.right, height)
        g.rectF(x - insets.left, y + height, width + insets.left + insets.right, insets.bottom)

        // Set backup paint.
        g.paint(oldPaint)
    }
}