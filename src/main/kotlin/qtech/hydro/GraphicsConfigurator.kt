package qtech.hydro

import qtech.bubbles.settings.GameSettings

class GraphicsConfigurator {
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