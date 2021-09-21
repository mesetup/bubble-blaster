package com.ultreon.bubbles.screen;

import com.ultreon.bubbles.BubbleBlaster;
import com.ultreon.bubbles.settings.GameSettings;
import com.ultreon.commons.lang.InfoTransporter;
import com.ultreon.hydro.Game;
import com.ultreon.hydro.render.Renderer;
import com.ultreon.hydro.screen.Screen;
import com.ultreon.hydro.util.GraphicsUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Environment loading scene.
 * When showing this scene, a new thread will be created and in that thread the loading will be done.
 * The thread is located in the method {@link #init()}.
 * <p>
 * Todo: update docstring.
 *
 * @author Qboi
 * @since 1.0.615
 */
public class MessageScreen extends Screen {
    private final InfoTransporter infoTransporter = new InfoTransporter(this::setDescription);
    private String description = "";
    protected BubbleBlaster game = BubbleBlaster.instance();

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void init() {

    }

    public InfoTransporter getInfoTransporter() {
        return infoTransporter;
    }

    /**
     * Renders the environment loading scene.<br>
     * Shows the title in the blue accent color (#00b0ff), and the description in a 50% black color (#7f7f7f).
     *
     * @param game the game launched.
     * @param gg   the graphics 2D processor.
     */
    @Override
    public void render(Game game, Renderer gg) {
        gg.color(new Color(64, 64, 64));
        gg.rect(0, 0, BubbleBlaster.instance().getWidth(), BubbleBlaster.instance().getHeight());
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        gg.color(new Color(0, 192, 255));
        GraphicsUtils.drawCenteredString(gg, "Loading Environment...", new Rectangle2D.Double(0, ((double) BubbleBlaster.instance().getHeight() / 2) - 24, BubbleBlaster.instance().getWidth(), 64d), new Font("Helvetica", Font.PLAIN, 48));
        gg.color(new Color(127, 127, 127));
        GraphicsUtils.drawCenteredString(gg, this.description, new Rectangle2D.Double(0, ((double) BubbleBlaster.instance().getHeight() / 2) + 40, BubbleBlaster.instance().getWidth(), 50d), new Font("Helvetica", Font.PLAIN, 20));
        if (GameSettings.instance().isTextAntialiasEnabled())
            gg.hint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
