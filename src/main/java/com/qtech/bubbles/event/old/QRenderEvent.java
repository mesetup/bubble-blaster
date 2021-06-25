package com.qtech.bubbles.event.old;

import com.qtech.bubbles.QBubbles;
import com.qtech.bubbles.common.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * <h1>Render Event</h1>
 * This event is for rendering the game objects on the canvas.
 *
 * @see QEvent
 * @see QTickEvent
 * @see Graphics
 * @see Graphics2D
 */
@Deprecated
public class QRenderEvent extends QEvent<QRenderEvent> {
    private static final QRenderEvent instance = new QRenderEvent();
    private final Graphics graphicsInstance;
    private final QBubbles main;
    private final Graphics2D graphics2DInstance;

    public QRenderEvent(QBubbles main, Scene scene, @NotNull Graphics g, Graphics2D g2d) {
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

    public Graphics getGraphicsInstance() {
        return graphicsInstance;
    }

    public QBubbles getMain() {
        return main;
    }

    public Graphics2D getGraphics2DInstance() {
        return graphics2DInstance;
    }
}
