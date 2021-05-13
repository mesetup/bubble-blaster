package com.qsoftware.bubbles.common.interfaces;

import com.qsoftware.bubbles.event.PauseTickEvent;

public interface PauseTickable {
    void onPauseUpdate(PauseTickEvent evt);
}
