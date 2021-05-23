package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.GraphicsProcessor;
import com.qtech.bubbleblaster.common.scene.Scene;


@Deprecated
public class HUDRenderEvent {
    private final Graphics graphicsInstance;
    private final GraphicsProcessor GraphicsProcessorInstance;
    private final GraphicsProcessor gfxProcessor;
    private final BubbleBlaster game;
    private final Scene scene;

    public HUDRenderEvent(BubbleBlaster game, Scene scene, GraphicsProcessor g2d) {
        this.game = game;
        this.scene = scene;

        GraphicsProcessor ngg = new GraphicsProcessor(g2d);
        ngg.setFallbackFont(BubbleBlaster.getInstance().getFont());
        this.graphicsInstance = ngg;
        this.GraphicsProcessorInstance = ngg;
        this.gfxProcessor = ngg;
    }

    public Graphics getGraphicsInstance() {
        return graphicsInstance;
    }

    public GraphicsProcessor getGraphicsProcessorInstance() {
        return GraphicsProcessorInstance;
    }

    public GraphicsProcessor getNewGraphicsProcessorInstance() {
        return gfxProcessor;
    }

    public BubbleBlaster getGame() {
        return game;
    }

    public Scene getScene() {
        return scene;
    }
}
