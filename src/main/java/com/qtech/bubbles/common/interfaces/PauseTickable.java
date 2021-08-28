package com.qtech.bubbles.common.interfaces;

import com.qtech.bubbles.event.PauseTickEvent;

@Deprecated
public interface PauseTickable {
    @Deprecated
    void onPauseUpdate(PauseTickEvent evt);
}
