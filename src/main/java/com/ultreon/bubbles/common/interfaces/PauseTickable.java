package com.ultreon.bubbles.common.interfaces;

import com.ultreon.hydro.event.PauseTickEvent;

@Deprecated
public interface PauseTickable {
    @Deprecated
    void onPauseUpdate(PauseTickEvent evt);
}
