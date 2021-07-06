package qtech.bubbles.event

import com.studiohartman.jamepad.ControllerManager
import com.studiohartman.jamepad.ControllerState

open class XInputEvent : Event {
    var isButtonA = false
        private set
    var isButtonB = false
        private set
    var isButtonX = false
        private set
    var isButtonY = false
        private set
    private var aJustPressed = false
    private var bJustPressed = false
    private var xJustPressed = false
    private var yJustPressed = false
    var isLeftButton = false
        private set
    var isRightButton = false
        private set
    var isLeftBtnJustPressed = false
        private set
    var isRightBtnJustPressed = false
        private set
    var leftTrigger = 0f
        private set
    var rightTrigger = 0f
        private set
    var leftStickX = 0f
        private set
    var leftStickY = 0f
        private set
    var leftStickAngle = 0f
        private set
    var isLeftStickClick = false
        private set
    var leftStickMagnitude = 0f
        private set
    var isLeftStickJustClicked = false
        private set
    var rightStickX = 0f
        private set
    var rightStickY = 0f
        private set
    var rightStickAngle = 0f
        private set
    var isRightStickClick = false
        private set
    var rightStickMagnitude = 0f
        private set
    var isRightStickJustClicked = false
        private set
    var isDpadUp = false
        private set
    var isDpadDown = false
        private set
    var isDpadLeft = false
        private set
    var isDpadRight = false
        private set
    var isDpadUpJustPressed = false
        private set
    var isDpadDownJustPressed = false
        private set
    var isDpadLeftJustPressed = false
        private set
    var isDpadRightJustPressed = false
        private set
    var isBack = false
        private set
    var isGuide = false
        private set
    var isStart = false
        private set
    var manager: ControllerManager? = null
        private set

    constructor(state: ControllerState, manager: ControllerManager?) {
        isButtonA = state.a
        isButtonB = state.b
        isButtonX = state.x
        isButtonY = state.y
        aJustPressed = state.aJustPressed
        bJustPressed = state.bJustPressed
        xJustPressed = state.xJustPressed
        yJustPressed = state.yJustPressed

        // Button L/R
        isLeftButton = state.lb
        isRightButton = state.rb

        // Button L/R Just Pressed
        isLeftBtnJustPressed = state.lbJustPressed
        isRightBtnJustPressed = state.rbJustPressed

        // Trigger L/R
        leftTrigger = state.leftTrigger
        rightTrigger = state.rightTrigger

        // Left Joystick
        leftStickX = state.leftStickX
        leftStickY = state.leftStickY
        leftStickAngle = state.leftStickAngle
        isLeftStickClick = state.leftStickClick
        leftStickMagnitude = state.leftStickMagnitude
        isLeftStickJustClicked = state.leftStickJustClicked

        // Right Joystick
        rightStickX = state.rightStickX
        rightStickY = state.rightStickY
        rightStickAngle = state.rightStickAngle
        isRightStickClick = state.rightStickClick
        rightStickMagnitude = state.rightStickMagnitude
        isRightStickJustClicked = state.rightStickJustClicked

        // D-Pad
        isDpadUp = state.dpadUp
        isDpadDown = state.dpadDown
        isDpadLeft = state.dpadLeft
        isDpadRight = state.dpadRight

        // D-Pad Just Pressed
        isDpadUpJustPressed = state.dpadUpJustPressed
        isDpadDownJustPressed = state.dpadDownJustPressed
        isDpadLeftJustPressed = state.dpadLeftJustPressed
        isDpadRightJustPressed = state.dpadRightJustPressed

        // Misc Button
        isBack = state.back
        isGuide = state.guide
        isStart = state.start

        // Controllers
        this.manager = manager
    }

    constructor()

    fun isaJustPressed(): Boolean {
        return aJustPressed
    }

    fun isbJustPressed(): Boolean {
        return bJustPressed
    }

    fun isxJustPressed(): Boolean {
        return xJustPressed
    }

    fun isyJustPressed(): Boolean {
        return yJustPressed
    }
}