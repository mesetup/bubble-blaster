package com.qtech.bubbles.ability.triggers;

import com.qtech.bubbles.common.ability.AbilityTrigger;
import com.qtech.bubbles.common.ability.AbilityTriggerType;
import com.qtech.bubbles.common.entity.Entity;
import com.qtech.bubbles.core.controllers.KeyboardController;
import com.qtech.bubbles.event.KeyboardEvent;
import com.qtech.bubbles.event.type.KeyEventType;

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
