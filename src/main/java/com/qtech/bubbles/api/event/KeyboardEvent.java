package com.qtech.bubbles.api.event;

import com.qtech.bubbles.api.event.keyboard.KeyboardModifiers;
import com.qtech.bubbles.event.type.KeyEventType;

public class KeyboardEvent extends Event<KeyboardEvent.KeyboardEventListener> {
    public KeyboardEvent() {
        super(KeyboardEventListener.class);
    }

    public interface KeyboardEventListener extends IListener {
        void onKeyboard(KeyEventType type, char key, int keyCode, KeyboardModifiers modifiers);
    }
}
