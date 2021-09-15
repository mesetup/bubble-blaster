package com.ultreon.bubbles.ability.triggers;

import com.ultreon.hydro.input.KeyboardController;
import com.ultreon.bubbles.entity.Entity;
import com.ultreon.bubbles.entity.player.ability.AbilityTrigger;
import com.ultreon.bubbles.entity.player.ability.AbilityTriggerType;
import com.ultreon.hydro.event.input.KeyboardEvent;
import com.ultreon.hydro.event.type.KeyEventType;

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
