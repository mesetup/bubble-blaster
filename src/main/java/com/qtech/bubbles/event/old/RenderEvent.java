package com.qtech.bubbles.event.old;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.scene.Scene;

@Deprecated
public class RenderEvent {
    private final GraphicsProcessor graphicsInstance;
    private final GraphicsProcessor graphics2DInstance;
    private final GraphicsProcessor gfxProcessor;
    private final BubbleBlaster game;
    private final Scene scene;

    public RenderEvent(BubbleBlaster game, Scene scene, GraphicsProcessor gp) {
        this.game = game;
        this.scene = scene;

        gp.setFallbackFont(BubbleBlaster.getInstance().getFont());
        this.graphicsInstance = gp;
        this.graphics2DInstance = gp;
        this.gfxProcessor = gp;
    }

    public GraphicsProcessor getGraphicsInstance() {
        return graphicsInstance;
    }

    public GraphicsProcessor getGraphics2DInstance() {
        return graphics2DInstance;
    }

    public GraphicsProcessor getNewGraphics2DInstance() {
        return gfxProcessor;
    }

    public BubbleBlaster getGame() {
        return game;
    }

    public Scene getScene() {
        return scene;
    }
}
