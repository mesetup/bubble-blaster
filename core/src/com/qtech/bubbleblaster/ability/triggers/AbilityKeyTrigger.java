package com.qtech.bubbleblaster.ability.triggers;

import com.qtech.bubbleblaster.common.ability.AbilityTrigger;
import com.qtech.bubbleblaster.common.ability.AbilityTriggerType;
import com.qtech.bubbleblaster.common.entity.Entity;
import com.qtech.bubbleblaster.core.controllers.KeyboardController;
import com.qtech.bubbleblaster.event.KeyboardEvent;
import com.qtech.bubbleblaster.event.type.KeyEventType;

import java.util.HashMap;

public class AbilityKeyTrigger extends AbilityTrigger {
    private final KeyboardController controller;
    private final int keyCode;
    private final HashMap<Integer, Boolean> currentlyPressed;
    private final KeyEventType keyEventType;

    public AbilityKeyTrigger(KeyboardEvent evt, Entity entity) {
        super(AbilityTriggerType.KEY_TRIGGER, entity);
        this.controller = evt.getController();
        this.keyCode = evt.getKeyCode();
        this.currentlyPressed = evt.getPressed();
        this.keyEventType = evt.getType();
    }

    public int getKeyCode() {
        return keyCode;
    }

    public HashMap<Integer, Boolean> getCurrentlyPressed() {
        return currentlyPressed;
    }

    public KeyboardController getController() {
        return controller;
    }

    public KeyEventType getKeyEventType() {
        return keyEventType;
    }
}
