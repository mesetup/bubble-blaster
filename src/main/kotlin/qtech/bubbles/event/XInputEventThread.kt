package qtech.bubbles.event

import qtech.bubbles.BubbleBlaster
import com.studiohartman.jamepad.ControllerManager

class XInputEventThread private constructor() : Runnable {
    private fun setStopFlag(stopFlag: Boolean) {
        this.stopFlag = stopFlag
    }

    companion object {
        private val controllers = ControllerManager()
        val instance = XInputEventThread()
        lateinit var thread: Thread
            private set

        fun start() {
            XInputEventThread()
        }

        init {
            controllers.initSDLGamepad()
        }
    }

    private var stopFlag = false
    override fun run() {
        while (!stopFlag) {
            doEvent()
        }
    }

    protected fun doEvent() {
        val currState = controllers.getState(0)

//        System.out.println(currState.isConnected);
        if (!currState.isConnected) {
            return
        }
        if (currState.a) {
            println("\"A\" on \"" + currState.controllerType + "\" is pressed")
        }
        BubbleBlaster.eventBus.post(XInputEvent(currState, controllers))
    }

    init {
        thread = Thread(this, "XInputEventThread")
    }
}