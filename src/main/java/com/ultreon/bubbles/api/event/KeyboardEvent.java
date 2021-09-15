package com.ultreon.bubbles.api.event;

import com.ultreon.bubbles.api.event.keyboard.KeyboardModifiers;
import com.ultreon.hydro.event.type.KeyEventType;

public class KeyboardEvent extends Event<KeyboardEvent.KeyboardEventListener> {
    public KeyboardEvent() {
        super(KeyboardEventListener.class);
    }

    public interface KeyboardEventListener extends IListener {
        void onKeyboard(KeyEventType type, char key, int keyCode, KeyboardModifiers modifiers);
    }
}
