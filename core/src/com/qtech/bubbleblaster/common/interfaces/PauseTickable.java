package com.qtech.bubbleblaster.common.interfaces;

import com.qtech.bubbleblaster.event.PauseTickEvent;

public interface PauseTickable {
    void onPauseUpdate(PauseTickEvent evt);
}
