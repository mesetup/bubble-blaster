package com.qsoftware.bubbles.event.old;

import com.qsoftware.bubbles.QBubbles;
import com.qsoftware.bubbles.common.scene.Scene;
import com.qsoftware.bubbles.core.controllers.MouseController;
import com.qsoftware.bubbles.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;

/**
 * <h1>Mouse Event</h1>
 * This event is used for handling mouse input.
 *
 * @see QKeyboardEvent
 */
@Deprecated
public class QMouseEvent extends QEvent<QMouseEvent> {
    private static final QMouseEvent instance = new QMouseEvent();
    private final QBubbles main;
    private final MouseController controller;
    private final MouseEvent parentEvent;
    private final MouseEventType type;

    public QMouseEvent(QBubbles main, Scene scene, @NotNull MouseController controller, MouseEvent event, MouseEventType type) {
//        super(getInstance().hashCode());
        this.main = main;
        this.controller = controller;
        this.parentEvent = event;
        this.type = type;

//        getInstance().call(this, scene);
    }

    protected QMouseEvent() {
//        super(new Random(System.nanoTime()).nextInt(Integer.MAX_VALUE));
        this.main = null;
        this.controller = null;
        this.parentEvent = null;
        this.type = null;
    }

    public static QMouseEvent getInstance() {
        return instance;
    }

    public QBubbles getMain() {
        return main;
    }

    @NotNull
    public MouseController getController() {
        return controller;
    }

    public MouseEvent getParentEvent() {
        return parentEvent;
    }

    public MouseEventType getType() {
        return type;
    }
}
