package com.qtech.bubbles.graphics

import com.qtech.bubbles.settings.GameSettings

class GraphicsEngine {
    private val antialiasingBackup = GameSettings.instance().isAntialiasEnabled
    var isAntialiasingEnabled = antialiasingBackup

    fun resetAntialiasing() {
        isAntialiasingEnabled = antialiasingBackup
    }

    fun enableAntialiasing() {
        isAntialiasingEnabled = true
    }

    fun disableAntialiasing() {
        isAntialiasingEnabled = false
    }

    fun drawBubble() {}
}