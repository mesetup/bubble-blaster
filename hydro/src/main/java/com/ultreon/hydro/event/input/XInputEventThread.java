package com.ultreon.hydro.event.input;

import com.ultreon.hydro.Game;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.ultreon.hydro.event.bus.GameEventBus;

public class XInputEventThread implements Runnable {
    private static final ControllerManager controllers = new ControllerManager();
    private static final XInputEventThread instance = new XInputEventThread();
    private static Thread thread;

    public static Thread getThread() {
        return thread;
    }

    protected void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    static {
        controllers.initSDLGamepad();
    }

    private boolean stopFlag;

    protected XInputEventThread() {
        thread = new Thread(this, "XInputEventThread");
    }

    public static XInputEventThread getInstance() {
        return instance;
    }

    public static void start() {
        new XInputEventThread();
    }

    @Override
    public void run() {
        while (!stopFlag) {
            doEvent();
        }
    }

    protected void doEvent() {
        ControllerState currState = controllers.getState(0);

//        System.out.println(currState.isConnected);

        if (!currState.isConnected) {
            return;
        }
        if (currState.a) {
            System.out.println("\"A\" on \"" + currState.controllerType + "\" is pressed");
        }

        GameEventBus.get().post(new XInputEvent(currState, controllers));
    }
}
