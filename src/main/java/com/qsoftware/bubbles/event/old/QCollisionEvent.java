package com.qsoftware.bubbles.event.old;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.common.scene.Scene;

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
public class QCollisionEvent extends QEvent<QCollisionEvent> {
    private static final QCollisionEvent instance = new QCollisionEvent();
    private final QBubbles main;
    private final Entity source;
    private final Entity target;

    public QCollisionEvent(QBubbles main, Scene scene, Entity source, Entity target) {
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

    public QBubbles getMain() {
        return main;
    }

    public Entity getSource() {
        return source;
    }

    public Entity getTarget() {
        return target;
    }
}
