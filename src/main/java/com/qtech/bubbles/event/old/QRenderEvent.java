package com.qtech.bubbles.event.old;

import com.qtech.bubbles.BubbleBlaster;
import com.qtech.bubbles.common.GraphicsProcessor;
import com.qtech.bubbles.common.scene.Scene;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Render Event</h1>
 * This event is for rendering the game objects on the canvas.
 *
 * @see QEvent
 * @see QTickEvent
 * @see GraphicsProcessor
 * @see GraphicsProcessor
 */
@Deprecated
public class QRenderEvent extends QEvent<QRenderEvent> {
    private static final QRenderEvent instance = new QRenderEvent();
    private final GraphicsProcessor graphicsInstance;
    private final BubbleBlaster main;
    private final GraphicsProcessor graphics2DInstance;

    public QRenderEvent(BubbleBlaster main, Scene scene, @NotNull GraphicsProcessor g, GraphicsProcessor g2d) {
//        super(getInstance().hashCode());
        this.graphicsInstance = g;
        this.graphics2DInstance = g2d;
        this.main = main;

//        getInstance().call(this, scene);
    }

    protected QRenderEvent() {
//        super(new Random(System.nanoTime()).nextInt(Integer.MAX_VALUE));

        this.graphicsInstance = null;
        this.graphics2DInstance = null;
        this.main = null;
    }

    public static QRenderEvent getInstance() {
        return instance;
    }

    public GraphicsProcessor getGraphicsInstance() {
        return graphicsInstance;
    }

    public BubbleBlaster getMain() {
        return main;
    }

    public GraphicsProcessor getGraphics2DInstance() {
        return graphics2DInstance;
    }
}
