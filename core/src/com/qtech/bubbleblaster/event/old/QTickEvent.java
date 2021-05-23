package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.scene.Scene;

/**
 * <h1>Update Event</h1>
 * This event is for updating values, or doing things such as collision.
 *
 * @see QEvent
 * @see QRenderEvent
 */
@Deprecated
public class QTickEvent extends QEvent<QTickEvent> {
    private static final QTickEvent instance = new QTickEvent();
    private final BubbleBlaster main;
    private final double deltaTime;

    public QTickEvent(BubbleBlaster main, Scene scene, double deltaTime) {
//        super(getInstance().hashCode());
        this.main = main;
        this.deltaTime = deltaTime;

//        getInstance().call(this, scene);
    }

    protected QTickEvent() {
//        super(new Random(System.nanoTime()).nextInt(Integer.MAX_VALUE));
        this.main = null;
        this.deltaTime = Double.NEGATIVE_INFINITY;
    }

    public static QTickEvent getInstance() {
        return instance;
    }

    public BubbleBlaster getMain() {
        return main;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
