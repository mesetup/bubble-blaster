package com.qtech.bubbles.environment;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.common.gamestate.GameEvent;
import com.qtech.bubbles.common.renderer.IRenderer;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EnvironmentRenderer implements IRenderer {
    public EnvironmentRenderer() {

    }

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
    public static void drawBubble(GraphicsProcessor g, int x, int y, int radius, Color... colors) {

        // Define ellipse-depth (pixels).
        double i = 0f;

        // Loop colors.
        for (Color color : colors) {
            // Set stroke width.
            if (i == 0) {
                if (colors.length > 1) {
                    g.setStroke(new BasicStroke(2.2f));
                } else {
                    g.setStroke(new BasicStroke(2.0f));
                }
            } else if (i == colors.length - 1) {
                g.setStroke(new BasicStroke(2.0f));
            } else {
                g.setStroke(new BasicStroke(2.2f));
            }

            // Set color.
            g.setColor(color);

            // Draw ellipse.
            Ellipse2D ellipse = getEllipse(x, y, radius, i);
            g.draw(ellipse);

            // Add 2 to ellipse-depth (pixels).
            i += 2f;
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
    protected static Ellipse2D getEllipse(double x, double y, double r, double i) {
        return new Ellipse2D.Double(x + i, y + i, r - i * 2f, r - i * 2f);
    }

    @Nullable
    public Environment getEnvironment() {
        if (BubbleBlaster.getInstance() == null) {
            return null;
        }

        return BubbleBlaster.getInstance().environment;
    }

    @Override
    public void render(GraphicsProcessor gg) {
        Environment environment = getEnvironment();
        if (environment == null) {
            return;
        }

        GameEvent currentGameEvent = environment.getCurrentGameEvent();
        if (currentGameEvent != null) {
            gg.setColor(currentGameEvent.getBackgroundColor());
        } else {
            gg.setColor(new Color(0, 96, 128));
        }
        gg.fillRect(0, 0, BubbleBlaster.getInstance().getWidth(), BubbleBlaster.getInstance().getHeight());

        environment.getGameType().render(gg);

        for (Entity entity : environment.getEntities()) {
            entity.renderEntity(gg);
        }

        environment.getGameType().renderGUI(gg);
        environment.getGameType().renderHUD(gg);
    }
}
