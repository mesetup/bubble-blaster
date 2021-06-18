package com.qtech.bubbles.environment

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.entity.Entity
import com.qtech.bubbles.common.renderer.IRenderer
import com.qtech.bubbles.graphics.GraphicsProcessor
import java.awt.BasicStroke
import java.awt.Color
import java.awt.geom.Ellipse2D

open class EnvironmentRenderer : IRenderer {
    private var oldEntities: MutableList<in Entity> = ArrayList()
    val environment: Environment?
        get() = BubbleBlaster.instance.environment

    override fun render(gg: GraphicsProcessor) {
        val environment = environment ?: return
        val currentGameEvent = environment.currentGameEvent
        if (currentGameEvent != null) {
            gg.color(currentGameEvent.backgroundColor)
        } else {
            gg.color(Color(0, 96, 128))
        }
        gg.rectF(0, 0, BubbleBlaster.instance.width, BubbleBlaster.instance.height)
        environment.gameMode.render(gg)
        val entities: MutableList<in Entity?> = ArrayList(environment.entities)
        if (environment.gameMode.isInitialized) {
            entities.removeIf { entity: Any? -> entity is Entity && oldEntities.contains(entity) }
        }
        for (entity in entities) {
            if (entity is Entity) {
                entity.renderEntity(gg)
            }
        }
        environment.gameMode.renderGUI(gg)
        environment.gameMode.renderHUD(gg)
    }

    companion object {
        /**
         * <h1>Draw bubble.</h1>
         * Draws bubble on screen.
         *
         * @param g      the graphics-2D instance
         * @param x      the x-coordinate.
         * @param y      the y-coordinate.
         * @param radius the bubble radius (full width.).
         * @param colors the bubble colors (on sequence).
         */
        @JvmStatic
        fun drawBubble(g: GraphicsProcessor, x: Int, y: Int, radius: Int, vararg colors: Color?) {

            // Define ellipse-depth (pixels).
            var i = 0.0

            // Loop colors.
            for (color in colors) {
                // Set stroke width.
                if (i == 0.0) {
                    if (colors.size > 1) {
                        g.stroke(BasicStroke(2.5f))
                    } else {
                        g.stroke(BasicStroke(2.0f))
                    }
                } else if (i == (colors.size - 1).toDouble()) {
                    g.stroke(BasicStroke(2.0f))
                } else {
                    g.stroke(BasicStroke(2.5f))
                }

                // Set color.
                g.color(color)

                // Draw ellipse.
                val ellipse = getEllipse(x.toDouble(), y.toDouble(), radius.toDouble(), i + 1.0f)
                g.shape(ellipse)

                // Add 2 to ellipse-depth (pixels).
                i += 2.0
            }
        }

        /**
         * <h1>Get Ellipse</h1>
         * Get ellipse from x, y, radius, delta-radius and delta-value.
         *
         * @param x the x-coordinate.
         * @param y the y-coordinate.
         * @param r the radius.
         * @param i the delta-radius.
         * @return the ellipse.
         */
        protected fun getEllipse(x: Double, y: Double, r: Double, i: Double): Ellipse2D {
            return Ellipse2D.Double(x + i, y + i, r - i * 2f, r - i * 2f)
        }
    }
}