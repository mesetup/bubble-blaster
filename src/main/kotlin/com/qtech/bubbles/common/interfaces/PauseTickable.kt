package com.qtech.bubbles.common.interfaces

import com.qtech.bubbles.event.GuiTickEvent

interface PauseTickable {
    fun onPauseUpdate(evt: GuiTickEvent?)
}