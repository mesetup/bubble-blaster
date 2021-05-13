package com.qsoftware.bubbles.event.old;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

@SuppressWarnings("unused")
@Deprecated
public class QXInputEvent {
    private boolean buttonA;
    private boolean buttonB;
    private boolean buttonX;
    private boolean buttonY;

    private boolean aJustPressed;
    private boolean bJustPressed;
    private boolean xJustPressed;
    private boolean yJustPressed;

    private boolean leftButton;
    private boolean rightButton;
    private boolean leftBtnJustPressed;
    private boolean rightBtnJustPressed;

    private float leftTrigger;
    private float rightTrigger;

    private float leftStickX;
    private float leftStickY;
    private float leftStickAngle;
    private boolean leftStickClick;
    private float leftStickMagnitude;
    private boolean leftStickJustClicked;

    private float rightStickX;
    private float rightStickY;
    private float rightStickAngle;
    private boolean rightStickClick;
    private float rightStickMagnitude;
    private boolean rightStickJustClicked;

    private boolean dpadUp;
    private boolean dpadDown;
    private boolean dpadLeft;
    private boolean dpadRight;

    private boolean dpadUpJustPressed;
    private boolean dpadDownJustPressed;
    private boolean dpadLeftJustPressed;
    private boolean dpadRightJustPressed;

    private boolean back;
    private boolean guide;
    private boolean start;
    private ControllerManager manager;

    public QXInputEvent(ControllerState state, ControllerManager manager) {
        this.buttonA = state.a;
        this.buttonB = state.b;
        this.buttonX = state.x;
        this.buttonY = state.y;

        this.aJustPressed = state.aJustPressed;
        this.bJustPressed = state.bJustPressed;
        this.xJustPressed = state.xJustPressed;
        this.yJustPressed = state.yJustPressed;

        // Button L/R
        this.leftButton = state.lb;
        this.rightButton = state.rb;

        // Button L/R Just Pressed
        this.leftBtnJustPressed = state.lbJustPressed;
        this.rightBtnJustPressed = state.rbJustPressed;

        // Trigger L/R
        this.leftTrigger = state.leftTrigger;
        this.rightTrigger = state.rightTrigger;

        // Left Joystick
        this.leftStickX = state.leftStickX;
        this.leftStickY = state.leftStickY;
        this.leftStickAngle = state.leftStickAngle;
        this.leftStickClick = state.leftStickClick;
        this.leftStickMagnitude = state.leftStickMagnitude;
        this.leftStickJustClicked = state.leftStickJustClicked;

        // Right Joystick
        this.rightStickX = state.rightStickX;
        this.rightStickY = state.rightStickY;
        this.rightStickAngle = state.rightStickAngle;
        this.rightStickClick = state.rightStickClick;
        this.rightStickMagnitude = state.rightStickMagnitude;
        this.rightStickJustClicked = state.rightStickJustClicked;

        // D-Pad
        this.dpadUp = state.dpadUp;
        this.dpadDown = state.dpadDown;
        this.dpadLeft = state.dpadLeft;
        this.dpadRight = state.dpadRight;

        // D-Pad Just Pressed
        this.dpadUpJustPressed = state.dpadUpJustPressed;
        this.dpadDownJustPressed = state.dpadDownJustPressed;
        this.dpadLeftJustPressed = state.dpadLeftJustPressed;
        this.dpadRightJustPressed = state.dpadRightJustPressed;

        // Misc Button
        this.back = state.back;
        this.guide = state.guide;
        this.start = state.start;

        // Controllers
        this.manager = manager;
    }

    public QXInputEvent() {

    }

    public boolean isButtonA() {
        return buttonA;
    }

    public boolean isButtonB() {
        return buttonB;
    }

    public boolean isButtonX() {
        return buttonX;
    }

    public boolean isButtonY() {
        return buttonY;
    }

    public boolean isaJustPressed() {
        return aJustPressed;
    }

    public boolean isbJustPressed() {
        return bJustPressed;
    }

    public boolean isxJustPressed() {
        return xJustPressed;
    }

    public boolean isyJustPressed() {
        return yJustPressed;
    }

    public boolean isLeftButton() {
        return leftButton;
    }

    public boolean isRightButton() {
        return rightButton;
    }

    public boolean isLeftBtnJustPressed() {
        return leftBtnJustPressed;
    }

    public boolean isRightBtnJustPressed() {
        return rightBtnJustPressed;
    }

    public float getLeftTrigger() {
        return leftTrigger;
    }

    public float getRightTrigger() {
        return rightTrigger;
    }

    public float getLeftStickX() {
        return leftStickX;
    }

    public float getLeftStickY() {
        return leftStickY;
    }

    public float getLeftStickAngle() {
        return leftStickAngle;
    }

    public boolean isLeftStickClick() {
        return leftStickClick;
    }

    public float getLeftStickMagnitude() {
        return leftStickMagnitude;
    }

    public boolean isLeftStickJustClicked() {
        return leftStickJustClicked;
    }

    public float getRightStickX() {
        return rightStickX;
    }

    public float getRightStickY() {
        return rightStickY;
    }

    public float getRightStickAngle() {
        return rightStickAngle;
    }

    public boolean isRightStickClick() {
        return rightStickClick;
    }

    public float getRightStickMagnitude() {
        return rightStickMagnitude;
    }

    public boolean isRightStickJustClicked() {
        return rightStickJustClicked;
    }

    public boolean isDpadUp() {
        return dpadUp;
    }

    public boolean isDpadDown() {
        return dpadDown;
    }

    public boolean isDpadLeft() {
        return dpadLeft;
    }

    public boolean isDpadRight() {
        return dpadRight;
    }

    public boolean isDpadUpJustPressed() {
        return dpadUpJustPressed;
    }

    public boolean isDpadDownJustPressed() {
        return dpadDownJustPressed;
    }

    public boolean isDpadLeftJustPressed() {
        return dpadLeftJustPressed;
    }

    public boolean isDpadRightJustPressed() {
        return dpadRightJustPressed;
    }

    public boolean isBack() {
        return back;
    }

    public boolean isGuide() {
        return guide;
    }

    public boolean isStart() {
        return start;
    }

    public ControllerManager getManager() {
        return manager;
    }
}
