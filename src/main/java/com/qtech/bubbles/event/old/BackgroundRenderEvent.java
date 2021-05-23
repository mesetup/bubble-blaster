package com.qtech.bubbles.event.old;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.scene.Scene;

import java.awt.*;

@Deprecated
public class BackgroundRenderEvent {
    private final Graphics graphicsInstance;
    private final Graphics2D graphics2DInstance;
    private final GraphicsProcessor gfxProcessor;
    private final QBubbles game;
    private final Scene scene;

    public BackgroundRenderEvent(QBubbles game, Scene scene, Graphics2D g2d) {
        this.game = game;
        this.scene = scene;

        GraphicsProcessor ngg = new GraphicsProcessor(g2d);
        ngg.setFallbackFont(QBubbles.getInstance().getFont());
        this.graphicsInstance = ngg;
        this.graphics2DInstance = ngg;
        this.gfxProcessor = ngg;
    }

    public Graphics getGraphicsInstance() {
        return graphicsInstance;
    }

    public Graphics2D getGraphics2DInstance() {
        return graphics2DInstance;
    }

    public GraphicsProcessor getNewGraphics2DInstance() {
        return gfxProcessor;
    }

    public QBubbles getGame() {
        return game;
    }

    public Scene getScene() {
        return scene;
    }
}
