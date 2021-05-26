package com.qtech.bubbles.common.interfaces;

import com.qtech.bubbles.event.PauseTickEvent;

public interface PauseTickable {
    void onPauseUpdate(PauseTickEvent evt);
}
