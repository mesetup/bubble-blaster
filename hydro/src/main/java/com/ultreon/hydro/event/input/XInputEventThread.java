package com.ultreon.hydro.event.input;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import com.ultreon.hydro.core.AntiMod;
import com.ultreon.hydro.event.bus.GameEvents;

@AntiMod
public class XInputEventThread implements Runnable {
    private static final ControllerManager controllers = new ControllerManager();
    private static final XInputEventThread instance = new XInputEventThread();
    private static Thread thread;

    private boolean stopFlag;

    static {
        controllers.initSDLGamepad();
    }

    public static Thread getThread() {
        return thread;
    }

    public static XInputEventThread instance() {
        return instance;
    }

    protected XInputEventThread() {
        thread = new Thread(this, "XInputEventThread");
    }

    protected void setStopFlag(boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public void start() {
        thread.start();
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

        GameEvents.get().publish(new XInputEvent(currState, controllers));
    }
}
