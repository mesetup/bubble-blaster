package com.qtech.bubbleblaster.event.old;

import com.qtech.bubbleblaster.BubbleBlaster;
import com.qtech.bubbleblaster.common.scene.Scene;
import com.qtech.bubbleblaster.core.controllers.MouseController;
import com.qtech.bubbleblaster.event.type.MouseEventType;
import org.jetbrains.annotations.NotNull;


/**
 * <h1>Mouse Event</h1>
 * This event is used for handling mouse input.
 *
 * @see QKeyboardEvent
 */
@Deprecated
public class QMouseEvent extends QEvent<QMouseEvent> {
    private static final QMouseEvent instance = new QMouseEvent();
    private final BubbleBlaster main;
    private final MouseController controller;
    private final MouseEvent parentEvent;
    private final MouseEventType type;

    public QMouseEvent(BubbleBlaster main, Scene scene, @NotNull MouseController controller, MouseEvent event, MouseEventType type) {
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

    public BubbleBlaster getMain() {
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
