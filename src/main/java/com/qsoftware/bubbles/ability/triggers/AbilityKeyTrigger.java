package com.qsoftware.bubbles.ability.triggers;

import com.qsoftware.bubbles.common.ability.AbilityTrigger;
import com.qsoftware.bubbles.common.ability.AbilityTriggerType;
import com.qsoftware.bubbles.common.entity.Entity;
import com.qsoftware.bubbles.core.controllers.KeyboardController;
import com.qsoftware.bubbles.event.KeyboardEvent;
import com.qsoftware.bubbles.event.type.KeyEventType;

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
