package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.common.scene.Scene;


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
public class QCollisionEvent extends QEvent<QCollisionEvent> {
    private static final QCollisionEvent instance = new QCollisionEvent();
    private final BubbleBlaster main;
    private final Entity source;
    private final Entity target;

    public QCollisionEvent(BubbleBlaster main, Scene scene, Entity source, Entity target) {
        super();
        this.source = source;
        this.target = target;
        this.main = main;

//        call(scene);
    }

    public QCollisionEvent() {
        super();
        source = null;
        target = null;
        main = null;
    }

    public static QCollisionEvent getInstance() {
        return instance;
    }

    public BubbleBlaster getMain() {
        return main;
    }

    public Entity getSource() {
        return source;
    }

    public Entity getTarget() {
        return target;
    }
}
