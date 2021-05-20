package com.qtech.bubbleblaster.event.old;

import com.studiohartman.jamepad.ControllerManager;

@Deprecated
public class QXInputEventThread extends QThreadedEvent<QXInputEvent> {
    private static final ControllerManager controllers = new ControllerManager();
    private static final QXInputEventThread instance = new QXInputEventThread();

    static {
        controllers.initSDLGamepad();
    }

    protected QXInputEventThread() {
//        super(QXInputEvent.class.hashCode());
    }

    public static QXInputEventThread getInstance() {
        return instance;
    }

    @Override
    protected void doEvent() {
//        ControllerState currState = controllers.getState(0);
//
////        System.out.println(currState.isConnected);
//
//        if(!currState.isConnected) {
//            return;
//        }
//        if(currState.a) {
//            System.out.println("\"A\" on \"" + currState.controllerType + "\" is pressed");
//        }
//        call(new QXInputEvent(currState, controllers), SceneManager.getInstance().getCurrentScene());
    }
}
