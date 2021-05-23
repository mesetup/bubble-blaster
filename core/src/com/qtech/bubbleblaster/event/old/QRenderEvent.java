package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.scene.Scene;
import org.jetbrains.annotations.NotNull;


/**
 * <h1>Render Event</h1>
 * This event is for rendering the game objects on the canvas.
 *
 * @see QEvent
 * @see QTickEvent
 * @see Graphics
 * @see GraphicsProcessor
 */
@Deprecated
public class QRenderEvent extends QEvent<QRenderEvent> {
    private static final QRenderEvent instance = new QRenderEvent();
    private final Graphics graphicsInstance;
    private final BubbleBlaster main;
    private final GraphicsProcessor GraphicsProcessorInstance;

    public QRenderEvent(BubbleBlaster main, Scene scene, @NotNull Graphics g, GraphicsProcessor g2d) {
//        super(getInstance().hashCode());
        this.graphicsInstance = g;
        this.GraphicsProcessorInstance = g2d;
        this.main = main;

//        getInstance().call(this, scene);
    }

    protected QRenderEvent() {
//        super(new Random(System.nanoTime()).nextInt(Integer.MAX_VALUE));

        this.graphicsInstance = null;
        this.GraphicsProcessorInstance = null;
        this.main = null;
    }

    public static QRenderEvent getInstance() {
        return instance;
    }

    public Graphics getGraphicsInstance() {
        return graphicsInstance;
    }

    public BubbleBlaster getMain() {
        return main;
    }

    public GraphicsProcessor getGraphicsProcessorInstance() {
        return GraphicsProcessorInstance;
    }
}
