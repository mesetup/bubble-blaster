package qtech.bubbles.common.interfaces

import qtech.bubbles.event.GuiTickEvent

interface PauseTickable {
    fun onPauseUpdate(evt: GuiTickEvent?)
}